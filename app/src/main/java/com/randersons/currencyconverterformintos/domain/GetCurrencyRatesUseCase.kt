package com.randersons.currencyconverterformintos.domain

import android.icu.math.BigDecimal
import com.randersons.currencyconverterformintos.Resource
import javax.inject.Inject

class GetCurrencyRatesUseCase @Inject constructor(
    private val frankfurterRepository: FrankfurterRepository
) {
    suspend operator fun invoke(): Resource<Map<String, BigDecimal>> {
        return frankfurterRepository.getCurrencyRates()
    }

    /*
     Adding the base currency from request (EUR) to top of the map,
     since it's used in the calculation of rates and default endpoint
     request value
     */
    fun sortRatesAndAddEUR(rates: Map<String, BigDecimal>) =
        mapOf("EUR" to BigDecimal("1.0")) + rates.toSortedMap()

}
