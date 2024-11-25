package com.randersons.currencyconverterformintos.domain.model

import android.icu.math.BigDecimal
import android.icu.math.MathContext
import android.icu.util.Currency
import java.util.Locale
import java.util.UUID

data class CurrencyData(
    val id: String = UUID.randomUUID().toString(),
    val currencyCode: String,
    val currencyFullName: String,
    val rate: BigDecimal,
    val convertedAmount: BigDecimal = BigDecimal.ZERO
) {
    companion object {
        fun create(
            currencyCode: String,
            rate: BigDecimal
        ): CurrencyData {
            val currency = Currency.getInstance(currencyCode)
            // Hardcoded to UK locale
            val currencyFullName = currency.getDisplayName(Locale.UK)

            return CurrencyData(
                currencyCode = currencyCode,
                currencyFullName = currencyFullName,
                rate = rate
            )
        }
    }
}

fun List<CurrencyData>.calculateConvertedAmounts(
    baseAmount: BigDecimal,
    apiRates: Map<String, BigDecimal>
) = map { currencyData ->
    val baseCurrency = this.firstOrNull()?.currencyCode ?: "EUR"
    val baseRate = apiRates[baseCurrency] ?: BigDecimal("1.0")
    val rate = apiRates[currencyData.currencyCode] ?: BigDecimal("1.0")

    val calculatedRate = rate
        .divide(baseRate, MathContext.ENGINEERING)
    val convertedAmount = baseAmount
        .multiply(calculatedRate)
        .setScale(6, BigDecimal.ROUND_HALF_UP)
    currencyData.copy(
        convertedAmount = convertedAmount,
        rate = calculatedRate
    )
}
