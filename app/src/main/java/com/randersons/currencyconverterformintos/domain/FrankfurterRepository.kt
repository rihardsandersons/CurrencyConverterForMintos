package com.randersons.currencyconverterformintos.domain

import android.icu.math.BigDecimal
import com.randersons.currencyconverterformintos.Resource

interface FrankfurterRepository {

   suspend fun getCurrencyRates(): Resource<Map<String, BigDecimal>>

}
