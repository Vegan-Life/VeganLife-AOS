package com.project.veganlife.login.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.project.veganlife.login.data.local.LocalUserDataSource
import com.project.veganlife.login.data.local.LocalUserDataSourceImpl
import com.project.veganlife.login.data.repositoryImpl.UserRepositoryImpl
import com.project.veganlife.login.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository {
        return userRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideEncryptedSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val prefFileName = "encrypted_prefs"

        return EncryptedSharedPreferences.create(
            context,
            prefFileName,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @Singleton
    fun provideLocalUserDataSource(
        sharedPreferences: SharedPreferences,
    ): LocalUserDataSource {
        return LocalUserDataSourceImpl(sharedPreferences)
    }
}