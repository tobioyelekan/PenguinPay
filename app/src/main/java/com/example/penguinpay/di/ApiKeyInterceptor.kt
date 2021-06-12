package com.example.penguinpay.di

import com.example.penguinpay.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiKeyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val httpUrl = request.url

        val url = httpUrl.newBuilder()
            .addQueryParameter("app_id", BuildConfig.API_KEY)
            .build()

        val requestBuilder = request.newBuilder()
            .url(url)
            .build()

        return chain.proceed(requestBuilder)
    }
}