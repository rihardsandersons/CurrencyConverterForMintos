package com.randersons.currencyconverterformintos.data.remote

import com.randersons.currencyconverterformintos.data.model.CurrencyApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FrankfurterApi {

    @GET("latest")
    suspend fun getLatestRates(
        @Query("base") baseCurrency: String
    ): CurrencyApiResponse

    companion object {
        const val BASE_URL = "https://api.frankfurter.app/"
    }
}
