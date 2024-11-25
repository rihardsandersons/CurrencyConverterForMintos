package com.randersons.currencyconverterformintos.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.randersons.currencyconverterformintos.presentation.theme.activeTileColor
import com.randersons.currencyconverterformintos.presentation.theme.tileBackgroundColor

/*
Specifically combined the base currency cell with other cells
for a simpler .animateItem() implementation to meet business logic
requirement of moving the clicked cell to top
 */
@Composable
fun CurrencyCell(
    modifier: Modifier = Modifier,
    rate: String,
    currencyCode: String,
    currencyFullName: String,
    convertedAmount: String,
    baseCurrency: String,
    baseAmount: String,
    isTopItem: Boolean = false,
    focusRequester: FocusRequester = FocusRequester(),
    focusManager: FocusManager = LocalFocusManager.current,
    decimalFormatter: DecimalFormatter = remember { DecimalFormatter() },
    onSelected: () -> Unit = {},
    onBaseAmountChanged: (String) -> Unit,
) {
    val tileBackground = if (isTopItem) activeTileColor else tileBackgroundColor
    val topTextColor = if (isTopItem) Color.White else Color.Black
    val bottomTextColor = if (isTopItem) Color.White else Color.Gray

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                clip = true
            )
            .clip(RoundedCornerShape(12.dp))
            .background(tileBackground)
            .clickable { onSelected() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = currencyCode,
                color = topTextColor
            )
            Text(
                text = currencyFullName,
                color = bottomTextColor
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            if (isTopItem) {
                CurrencyInputTextField(
                    baseAmount = baseAmount,
                    focusRequester = focusRequester,
                    focusManager = focusManager,
                    decimalFormatter = decimalFormatter,
                    onBaseAmountChanged = onBaseAmountChanged
                )
            } else {
                Text(
                    text = convertedAmount,
                    color = topTextColor
                )
                Text(
                    text = "1 $baseCurrency = $rate",
                    color = bottomTextColor
                )
            }

        }
    }
}

@Composable
fun InitialFadeIn(
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) {
    var visibility by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit, block = { visibility = true })
    AnimatedVisibility(
        visible = visibility,
        enter = slideInVertically() ,
        content = content
    )
}


@Preview
@Composable
fun PreviewCurrencyCellSelected() {
    CurrencyCell(
        rate = "2.5",
        currencyCode = "AUD",
        currencyFullName = "Australian Dollar",
        convertedAmount = "20.01",
        baseCurrency = "EUR",
        baseAmount = "213",
        isTopItem = true,
        onSelected = {},
        onBaseAmountChanged = {}
    )
}

@Preview
@Composable
fun PreviewCurrencyCell() {
    CurrencyCell(
        rate = "2.5",
        currencyCode = "AUD",
        currencyFullName = "Australian Dollar",
        convertedAmount = "20.01",
        baseCurrency = "EUR",
        baseAmount = "213",
        onSelected = {},
        onBaseAmountChanged = {}
    )
}
