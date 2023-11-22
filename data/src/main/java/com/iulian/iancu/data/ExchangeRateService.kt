package com.iulian.iancu.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateService {

    @GET("simple/price")
    suspend fun getExchangeRates(
        @Query("ids") id: String = "ethereum",
        @Query("vs_currencies") currency: String
    ): Response<ExchangeRateResponse>

    companion object {
        var retrofitService: ExchangeRateService? = null
        fun getInstance(): ExchangeRateService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.coingecko.com/api/v3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(ExchangeRateService::class.java)
            }
            return retrofitService!!
        }
    }
}