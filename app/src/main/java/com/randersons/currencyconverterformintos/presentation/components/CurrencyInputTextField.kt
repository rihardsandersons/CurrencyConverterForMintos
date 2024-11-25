package com.randersons.currencyconverterformintos.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CurrencyInputTextField(
    baseAmount: String,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    decimalFormatter: DecimalFormatter = remember { DecimalFormatter() },
    onBaseAmountChanged: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()

    TextField(
        value = baseAmount,
        onValueChange = { newValue ->
            onBaseAmountChanged(decimalFormatter.cleanup(newValue))
        },
        keyboardOptions =
        KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                scope.launch {
                    delay(100L)
                    focusManager.clearFocus()
                }
            }),
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        textStyle = TextStyle(
            textAlign = TextAlign.End,
            fontSize = 22.sp,
            lineHeight = 28.sp,
        ),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
}
