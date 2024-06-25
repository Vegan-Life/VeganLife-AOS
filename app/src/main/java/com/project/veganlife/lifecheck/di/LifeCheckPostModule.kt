package com.project.veganlife.lifecheck.di

import com.project.veganlife.lifecheck.data.remote.LifeCheckMealDataPostApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class LifeCheckPostModule {


    @Provides
    @Named("lifeCheckRetrofit")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dev.konggogi.store/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideLifeCheckMealDataPostApi(@Named("lifeCheckRetrofit") retrofit: Retrofit): LifeCheckMealDataPostApi {
        return retrofit.create(LifeCheckMealDataPostApi::class.java)
    }

    private val nullOnEmptyConverterFactory = object : Converter.Factory() {
        fun converterFactory() = this
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit
        ) = object : Converter<ResponseBody, Any?> {
            val nextResponseBodyConverter =
                retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)

            override fun convert(value: ResponseBody) =
                if (value.contentLength() == 0L) null else nextResponseBodyConverter.convert(value)
        }
    }

}