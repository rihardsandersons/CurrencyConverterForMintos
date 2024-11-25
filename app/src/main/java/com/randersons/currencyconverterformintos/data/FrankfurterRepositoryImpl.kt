package com.randersons.currencyconverterformintos.data

import android.icu.math.BigDecimal
import com.randersons.currencyconverterformintos.Resource
import com.randersons.currencyconverterformintos.data.remote.FrankfurterApi
import com.randersons.currencyconverterformintos.domain.FrankfurterRepository
import javax.inject.Inject

private const val BASE_CURRENCY = "EUR"

class FrankfurterRepositoryImpl @Inject constructor(
    private val api: FrankfurterApi
) : FrankfurterRepository {

    override suspend fun getCurrencyRates(): Resource<Map<String, BigDecimal>> {
        return try {
            val response = api.getLatestRates(BASE_CURRENCY)
            val rates: Map<String, BigDecimal> = response.rates.mapValues {
                BigDecimal(it.value)
            }
            Resource.Success(rates)
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Error getting currency rates")
        }
    }
}

/*
just for composable previews
 */
val mockMap: Map<String, BigDecimal> = mapOf(
    "EUR" to BigDecimal("1.0"),
    "AUD" to BigDecimal("1.6018"),
    "BGN" to BigDecimal("1.9558"),
    "BRL" to BigDecimal("6.0483"),
    "CAD" to BigDecimal("1.4561"),
    "CHF" to BigDecimal("0.9272"),
    "CNY" to BigDecimal("7.5458"),
    "CZK" to BigDecimal("25.358"),
    "DKK" to BigDecimal("7.4577"),
    "GBP" to BigDecimal("0.83205"),
    "HKD" to BigDecimal("8.1052"),
    "HUF" to BigDecimal("411.13"),
    "IDR" to BigDecimal("16570"),
    "ILS" to BigDecimal("3.8717"),
    "INR" to BigDecimal("87.93"),
    "ISK" to BigDecimal("145.7"),
    "JPY" to BigDecimal("160.84"),
)
