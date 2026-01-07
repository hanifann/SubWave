package com.hanifan.subwave.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.hanifan.subwave.core.network.client
import com.hanifan.subwave.common.Constant
import com.hanifan.subwave.core.network.ClientInterceptor
import com.hanifan.subwave.core.storage.AppDatabase
import com.hanifan.subwave.core.storage.SqlCipherKeyManager
import com.hanifan.subwave.data.home.datasource.HomeRemoteDataSource
import com.hanifan.subwave.data.login.datasource.LoginRemoteDataSource
import com.hanifan.subwave.domain.login.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideClientInterceptor(
        userRepository: UserRepository
    ): Interceptor {
        return ClientInterceptor(userRepository)
    }

    @Provides
    @Singleton
    fun providerRetrofitClient(
        interceptor: Interceptor
    ): Retrofit {
        return client(interceptor)
    }

    @Provides
    @Singleton
    fun provideLoginRemoteDataSource(httpClient: Retrofit): LoginRemoteDataSource {
        return httpClient.create(LoginRemoteDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideTopSongRemoteDataSource(httpClient: Retrofit): HomeRemoteDataSource {
        return httpClient.create(HomeRemoteDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create (
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { context.preferencesDataStoreFile(Constant.DATASTORE_NAME)}
        )
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context,
        keyManager: SqlCipherKeyManager
    ): AppDatabase {
        System.loadLibrary(Constant.SQLCIPHER_LIB_NAME)
        val room = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            Constant.DATABASE_NAME
        )
            .openHelperFactory(keyManager.getSupportFactory())
            .build()
        return room
    }
}