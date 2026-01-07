package com.hanifan.subwave.core.network

import android.os.Build
import com.hanifan.subwave.common.Constant
import com.hanifan.subwave.domain.login.repository.UserRepository
import com.squareup.moshi.Moshi
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.MessageDigest
import javax.inject.Inject
import kotlin.random.Random

fun client(clientInterceptor: Interceptor): Retrofit {
    val moshi = Moshi.Builder()
        .add(ApiResponseAdapter())
        .build()

    val client = OkHttpClient.Builder()
        .addInterceptor(clientInterceptor)
        .addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    return Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

}

class ClientInterceptor @Inject constructor(
    private val userRepository: UserRepository
) : Interceptor {
    companion object {
        val CLIENT: String? = Build.DEVICE ?: "Unknown"
        const val API_VERSION = "1.16.1"
        const val RESPONE_TYPE = "json"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        // getting cached data from room
        val cachedData = userRepository.getCachedUser()

        val origRequest = chain.request()
        val password = origRequest.url.queryParameter("p") ?: "password"

        val (salt, token) = if (cachedData?.token.isNullOrEmpty() || cachedData.salt.isEmpty()) {
            val newSalt = generateRandomSalt()
            val newToken = md5(password + newSalt)
            newSalt to newToken
        } else {
            cachedData.salt to cachedData.token

        }

        val httpUrl = cachedData?.serverUrl?.toHttpUrl() ?: origRequest.url

        // generate new request
        val newUrlbuilder = origRequest.url.newBuilder()
            .removeAllQueryParameters("p")
            .scheme(httpUrl.scheme)
            .host(httpUrl.host)
            .port(httpUrl.port)
            .encodedPath("/rest${origRequest.url.encodedPath}")
            .addQueryParameter("f", RESPONE_TYPE)
            .addQueryParameter("c", CLIENT)
            .addQueryParameter("v", API_VERSION)
            .addQueryParameter("t", token)
            .addQueryParameter("s", salt)

        val username = origRequest.url.queryParameter("u") ?: cachedData?.username
        if(!username.isNullOrEmpty()) {
            newUrlbuilder.addQueryParameter("u", username)
        }
        val newRequest = origRequest.newBuilder()
            .url(newUrlbuilder.build())
            .build()

        return chain.proceed(newRequest)

    }

    private fun generateRandomSalt(length: Int = 12): String {
        val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return  (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString ("")
    }

    private fun md5(input: String): String {
        val bytes = MessageDigest.getInstance("md5")
            .digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}