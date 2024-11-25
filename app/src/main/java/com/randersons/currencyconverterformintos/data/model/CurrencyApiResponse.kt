package com.randersons.currencyconverterformintos.data.model

data class CurrencyApiResponse(
    val amount: String,
    val base: String,
    val date: String,
    val rates: Map<String, String>
)
