package com.randersons.currencyconverterformintos.presentation

import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.randersons.currencyconverterformintos.data.mockMap
import com.randersons.currencyconverterformintos.domain.model.CurrencyData
import com.randersons.currencyconverterformintos.presentation.components.CurrencyCell
import com.randersons.currencyconverterformintos.presentation.components.InitialFadeIn
import com.randersons.currencyconverterformintos.presentation.theme.converterBackgroundColor
import com.randersons.currencyconverterformintos.presentation.theme.topBarBackgroundColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale as DecimalLocale

private const val API_FETCH_DELAY = 3000L

@Composable
fun ConverterScreen(
    modifier: Modifier = Modifier,
    converterViewModel: ConverterViewModel = hiltViewModel()
) {
    val currencyState by converterViewModel.uiCurrencyState.collectAsStateWithLifecycle()
    val currencyListWithLiveRates by converterViewModel.currencyListWithLiveRates.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LifecycleResumeEffect(Unit) {
        val job = scope.launch {
            while (true) {
                converterViewModel.fetchRates()
                delay(API_FETCH_DELAY)
            }
        }

        onPauseOrDispose {
            job.cancel()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(converterBackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (currencyListWithLiveRates.isEmpty()) {
            CircularProgressIndicator(
                color = topBarBackgroundColor
            )
        } else {
            InitialFadeIn {
                ConverterScreen(
                    modifier = modifier,
                    currencyList = currencyListWithLiveRates,
                    baseAmount = currencyState.baseAmount,
                    focusRequester = focusRequester,
                    focusManager = focusManager,
                    onBaseAmountChanged = { converterViewModel.updateBaseAmount(it) },
                    onSelected = { converterViewModel.moveToTop(it) }
                )
            }
        }
    }
}


@Composable
fun ConverterScreen(
    modifier: Modifier = Modifier,
    currencyList: List<CurrencyData>,
    baseAmount: String,
    focusRequester: FocusRequester, focusManager: FocusManager,
    onSelected: (String) -> Unit,
    onBaseAmountChanged: (String) -> Unit
) {
    val listState = rememberLazyListState()
    val decimalFormat = remember { DecimalFormat("0.00", DecimalFormatSymbols(DecimalLocale.UK)) }

    /*
    To not open the keyboard focus on initial app launch
     */
    var selectionInitiated by remember { mutableStateOf(false) }

    LaunchedEffect(currencyList.first().id, selectionInitiated) {
        if (selectionInitiated) {
            listState.animateScrollToItem(0)
            delay(100L)
            focusRequester.requestFocus()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        /*
        This is a fast workaround for unwanted scroll when reording/reassigning the list
        https://issuetracker.google.com/issues/234223556#comment2
         */
        item(key = 0) {
            Spacer(Modifier.height(1.dp))
        }

        items(
            count = currencyList.size,
            key = { index -> currencyList[index].id }
        ) { index ->
            val currencyItem = currencyList[index]
            val isTopItem = index == 0

            val formattedRate = remember(currencyItem.rate) {
                decimalFormat.format(currencyItem.rate.toDouble())
            }
            val formattedConvertedAmount = remember(currencyItem.convertedAmount) {
                decimalFormat.format(currencyItem.convertedAmount.toDouble())
            }

            CurrencyCell(
                modifier = Modifier.animateItem(),
                currencyCode = currencyItem.currencyCode,
                currencyFullName = currencyItem.currencyFullName,
                rate = formattedRate,
                convertedAmount = formattedConvertedAmount,
                baseCurrency = currencyList[0].currencyCode,
                isTopItem = isTopItem,
                focusRequester = focusRequester,
                focusManager = focusManager,
                baseAmount = baseAmount,
                onSelected = {
                    onSelected(currencyItem.id)
                    selectionInitiated = true
                },
                onBaseAmountChanged = onBaseAmountChanged
            )
        }
    }
}

@Preview
@Composable
fun PreviewConverterScreen() {
    ConverterScreen(
        currencyList = mockMap.map {
            CurrencyData.create(
                currencyCode = it.key,
                rate = it.value
            )
        },
        baseAmount = "213",
        focusRequester = FocusRequester(),
        focusManager = LocalFocusManager.current,
        onSelected = {},
        onBaseAmountChanged = {}
    )
}
