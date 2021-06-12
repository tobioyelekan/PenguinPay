package com.example.penguinpay.di

import com.example.penguinpay.BuildConfig
import com.example.penguinpay.BuildConfig.BASE_URL
import com.example.penguinpay.data.source.remote.RateService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun providesRateService(retrofit: Retrofit): RateService {
        return retrofit.create(RateService::class.java)
    }

    @Provides
    @Singleton
    fun providesRetrofit(apiKeyInterceptor: ApiKeyInterceptor): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(apiKeyInterceptor)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(interceptor)
        }

        return Retrofit.Builder()
            .client(builder.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }
}