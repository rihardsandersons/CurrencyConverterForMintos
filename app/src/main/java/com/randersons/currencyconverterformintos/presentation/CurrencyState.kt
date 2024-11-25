package com.randersons.currencyconverterformintos.presentation

import androidx.compose.runtime.Stable
import com.randersons.currencyconverterformintos.domain.model.CurrencyData

@Stable
data class CurrencyState(
    val currencyList: List<CurrencyData> = emptyList(),
    val baseAmount: String = "1.0"
)
