package com.randersons.currencyconverterformintos.presentation.components

import android.icu.text.DecimalFormatSymbols
/*
Found multiple articles how to deal with currency formatting in compose TextField.
All implementations were very similar and just used this one.

https://dev.to/tuvakov/decimal-input-formatting-with-jetpack-composes-visualtransformation-110n
 */

class DecimalFormatter(
    symbols: DecimalFormatSymbols = DecimalFormatSymbols.getInstance()
) {
    private val decimalSeparator = symbols.decimalSeparator

    fun cleanup(input: String): String {

        if (input.matches("\\D".toRegex())) return ""
        if (input.matches("0+".toRegex())) return "0"

        val sb = StringBuilder()

        var hasDecimalSep = false

        for (char in input) {
            if (char.isDigit()) {
                sb.append(char)
                continue
            }
            if (char == decimalSeparator && !hasDecimalSep && sb.isNotEmpty()) {
                sb.append(char)
                hasDecimalSep = true
            }
        }

        return sb.toString()
    }
}
