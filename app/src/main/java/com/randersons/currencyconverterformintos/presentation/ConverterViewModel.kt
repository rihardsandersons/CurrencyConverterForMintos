package com.randersons.currencyconverterformintos.presentation

import android.icu.math.BigDecimal
import android.icu.text.DecimalFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.randersons.currencyconverterformintos.Resource
import com.randersons.currencyconverterformintos.domain.GetCurrencyRatesUseCase
import com.randersons.currencyconverterformintos.domain.model.CurrencyData
import com.randersons.currencyconverterformintos.domain.model.calculateConvertedAmounts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase
) : ViewModel() {

    private val apiRates = MutableStateFlow<Map<String, BigDecimal>>(emptyMap())
    private val _uiCurrencyState = MutableStateFlow(CurrencyState())
    val uiCurrencyState: StateFlow<CurrencyState> = _uiCurrencyState.asStateFlow()

    val currencyListWithLiveRates: StateFlow<List<CurrencyData>> = combine(
        apiRates,
        uiCurrencyState
    ) { apiRates, converterState ->

        val bigDecimalBaseAmount = BigDecimal(
            if (converterState.baseAmount.toBigDecimalOrNull() != null) {
                converterState.baseAmount
            } else {
                "0"
            }
        )
        converterState.currencyList.calculateConvertedAmounts(
            bigDecimalBaseAmount,
            apiRates
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun fetchRates() {
        viewModelScope.launch {
            when (val result = getCurrencyRatesUseCase()) {
                is Resource.Success -> {
                    result.data?.let { bigDecimalMap ->
                        if (uiCurrencyState.value.currencyList.isEmpty()) {
                            loadInitialCurrenciesList(bigDecimalMap)
                        }
                        apiRates.update { bigDecimalMap }
                    }
                }

                is Resource.Error -> {
                    val errorMessage = result.message
                    // TODO: error handling
                }
            }
        }
    }

    fun updateBaseAmount(newAmount: String) {
        _uiCurrencyState.update {
            it.copy(baseAmount = newAmount)
        }
    }

    private fun loadInitialCurrenciesList(apiRates: Map<String, BigDecimal>) {
        val sortedApiRates = getCurrencyRatesUseCase.sortRatesAndAddEUR(apiRates)
        _uiCurrencyState.value = CurrencyState(
            currencyList = sortedApiRates.map {
                CurrencyData.create(
                    currencyCode = it.key,
                    rate = it.value
                )
            }
        )
    }

    fun moveToTop(id: String) {
        val currentList = currencyListWithLiveRates.value
        val itemToMove = currentList.find { it.id == id }
        if (itemToMove != null) {
            val newList = listOf(itemToMove) + currentList
                .filterNot { it.id == id }
                .sortedBy { it.currencyCode }

            val decimalFormat = DecimalFormat("#.##")
            val baseAmount = decimalFormat.format(itemToMove.convertedAmount.toDouble())
            _uiCurrencyState.update {
                it.copy(
                    currencyList = newList,
                    baseAmount = baseAmount
                )
            }
        }
    }
}
