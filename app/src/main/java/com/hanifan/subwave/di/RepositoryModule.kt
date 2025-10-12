package com.hanifan.subwave.di

import com.hanifan.subwave.core.storage.DataStoreHelper
import com.hanifan.subwave.core.storage.DataStoreHelperImpl
import com.hanifan.subwave.core.storage.SqlCipherKeyManager
import com.hanifan.subwave.core.storage.SqlCipherKeyManagerHelper
import com.hanifan.subwave.data.login.data_source.LoginLocalDataSource
import com.hanifan.subwave.data.login.data_source.LoginLocalDataSourceImpl
import com.hanifan.subwave.data.login.repository.LoginRepositoryImpl
import com.hanifan.subwave.data.login.repository.UserRepositoryImpl
import com.hanifan.subwave.domain.login.repository.LoginRepository
import com.hanifan.subwave.domain.login.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsLoginRepository(impl: LoginRepositoryImpl): LoginRepository

    @Binds
    @Singleton
    abstract fun bindsUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindsDataStoreHelper(impl: DataStoreHelperImpl): DataStoreHelper

    @Binds
    @Singleton
    abstract fun bindsSqlCipherKeyManager(impl: SqlCipherKeyManagerHelper): SqlCipherKeyManager

    @Binds
    @Singleton
    abstract fun bindsLoginLocalDataSource(impl: LoginLocalDataSourceImpl): LoginLocalDataSource
}