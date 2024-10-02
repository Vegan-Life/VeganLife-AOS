package com.project.veganlife.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.project.veganlife.BuildConfig
import com.project.veganlife.data.datasource.TokenManager
import com.project.veganlife.data.datasource.TokenManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

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
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASEURL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authorizationInterceptor: Interceptor,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        builder.apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(authorizationInterceptor)
            connectTimeout(0, TimeUnit.SECONDS) // SSE를 위한 타임아웃 해제
            readTimeout(0, TimeUnit.SECONDS)    // SSE를 위한 타임아웃 해제
        }

        return  builder.build()
    }

    @Singleton
    @Provides
    fun providesAuthorizationInterceptor(
        tokenManager: TokenManager,
    ) = Interceptor { chain ->
        val accessToken = tokenManager.getAccessToken()
        val request = chain.request().newBuilder()
            .header("Authorization", "$accessToken")
            .build()

        chain.proceed(request)
    }

    @Provides
    @Singleton
    fun provideTokenManager(
        sharedPreferences: SharedPreferences
    ): TokenManager {
        return TokenManagerImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }
}