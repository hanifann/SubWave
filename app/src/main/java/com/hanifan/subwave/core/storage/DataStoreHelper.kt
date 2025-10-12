package com.hanifan.subwave.core.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface DataStoreHelper {
    suspend fun getData(key: String): String
    suspend fun saveData(data: String, key: String)
}

class DataStoreHelperImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): DataStoreHelper {
    override suspend fun getData(key: String): String {
        val preferences = dataStore.data
            .catch { emptyPreferences() }
            .first()
        return preferences[stringPreferencesKey(key)] ?: ""
    }

    override suspend fun saveData(
        data: String,
        key: String,
    ) {
        dataStore.edit {
            it[stringPreferencesKey(key)] = data
        }
    }
}