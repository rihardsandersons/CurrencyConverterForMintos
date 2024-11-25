package com.randersons.currencyconverterformintos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import com.randersons.currencyconverterformintos.presentation.ConverterScreen
import com.randersons.currencyconverterformintos.presentation.theme.CurrencyConverterForMintosTheme
import com.randersons.currencyconverterformintos.presentation.theme.statusBarColor
import com.randersons.currencyconverterformintos.presentation.theme.topBarBackgroundColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                statusBarColor.toArgb(),
                statusBarColor.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.dark(
                android.graphics.Color.BLACK
            )
        )

        /*
        Since it's only one screen, did not include any navigation.
         */
        setContent {
            CurrencyConverterForMintosTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = { CurrencyTopBar() }
                ) { innerPadding ->
                    ConverterScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "Currency Converter",
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = topBarBackgroundColor,
            titleContentColor = Color.White
        )
    )
}

// TODO: formatting
// TODO: unittest
