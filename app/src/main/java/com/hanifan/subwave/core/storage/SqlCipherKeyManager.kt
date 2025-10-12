package com.hanifan.subwave.core.storage

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

interface SqlCipherKeyManager {
    fun getSupportFactory(): SupportOpenHelperFactory
}

class SqlCipherKeyManagerHelper @Inject constructor(
    private val dataStore: DataStoreHelper
) : SqlCipherKeyManager {
    companion object {
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val ENCRYPTED_KEY = "ENCRYPTED_KEY"
        const val ENCRYPTED_IV_KEY = "ENCRYPTED_IV_KEY"
        const val SQLCIPHER_KEY = "SQLCIPHER_KEY"
    }

    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        .apply {load(null)}

    private suspend fun ensureKeyInitialized() {
        generateKeystoreKeyIfNeeded()
        if(dataStore.getData(ENCRYPTED_KEY).isEmpty()) {
            generateEncryptedSqlCipherKey()
        }
    }

    override fun getSupportFactory(): SupportOpenHelperFactory = runBlocking (Dispatchers.IO) {
        ensureKeyInitialized()
        val encryptedKey = dataStore.getData(ENCRYPTED_KEY)
        val iv = dataStore.getData(ENCRYPTED_IV_KEY)
        val decryptedKey = getDecryptedSqlChiperKey(encryptedKey, iv)
        DisposableKeySupportFactory(decryptedKey)
    }

    private fun getSecretKey(): SecretKey =
        (keyStore.getEntry(SQLCIPHER_KEY, null) as KeyStore.SecretKeyEntry).secretKey

    private fun getDecryptedSqlChiperKey(key: String, iv: String): ByteArray {
        val encryptedKey = Base64.decode(key, Base64.NO_WRAP)
        val ivBytes = Base64.decode(iv, Base64.NO_WRAP)

        val secretKey = getSecretKey()
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, ivBytes))

        return cipher.doFinal(encryptedKey)
    }

    private fun generateKeystoreKeyIfNeeded() {
        if (!keyStore.containsAlias(SQLCIPHER_KEY)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            val keyGenSpec = KeyGenParameterSpec.Builder(
                    SQLCIPHER_KEY,
                    KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT
                )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
            keyGenerator.init(keyGenSpec)
            keyGenerator.generateKey()
        }
    }

    private suspend fun generateEncryptedSqlCipherKey() {
        val secretKey = getSecretKey()
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val sqlCipherKey = ByteArray(32)
        SecureRandom().nextBytes(sqlCipherKey)

        val encryptedKey = cipher.doFinal(sqlCipherKey)
        val iv = cipher.iv

        dataStore.saveData(
            key = ENCRYPTED_KEY,
            data = Base64.encodeToString(encryptedKey, Base64.NO_WRAP)
        )
        dataStore.saveData(
            key = ENCRYPTED_IV_KEY,
            data = Base64.encodeToString(iv, Base64.NO_WRAP)
        )

        sqlCipherKey.fill(0)
    }
}