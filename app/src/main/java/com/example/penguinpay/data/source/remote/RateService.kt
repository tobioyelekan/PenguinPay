package com.example.penguinpay.data.source.remote

import retrofit2.http.GET

interface RateService {
    @GET("latest.json")
    suspend fun getCurrencyRates(): String
}