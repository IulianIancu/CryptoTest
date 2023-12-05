@file:OptIn(ExperimentalMaterial3Api::class)

package com.iulian.iancu.cryptoscreenapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iulian.iancu.cryptoscreenapp.ui.theme.CryptoScreenAppTheme
import com.iulian.iancu.domain.ExchangeRate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoScreenAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            MainScreen(viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: IMainViewModel) {
    val sheetState = rememberModalBottomSheetState()
    val isSheetOpen = viewModel.isCurrencySelectorOpen.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = CenterHorizontally
    ) {
        Text(
            text = "Transfer to Greg",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            InputFields(viewModel = viewModel)

            SwapButton(viewModel = viewModel)

            CurrencyButton(viewModel = viewModel)
        }

        val context = LocalContext.current
        Button(onClick = { Toast.makeText(context, "Money Sent!", Toast.LENGTH_LONG).show() }) {
            Text(text = "Transfer to Greg")
        }
    }

    if (isSheetOpen.value) {
        ModalBottomSheet(onDismissRequest = { viewModel.setIsCurrencySelectorOpen(false) }) {
            val currencies = viewModel.currencyRateList.collectAsState()
            currencies.value.forEach { exchangeRate ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clickable {
                        viewModel.setCurrentExchangeRate(exchangeRate)
                        viewModel.setIsCurrencySelectorOpen(false)
                    }) {
                    Icon(
                        painter = painterResource(
                            id = when (exchangeRate) {
                                is ExchangeRate.EUR -> R.drawable.ic_eu_flag
                                is ExchangeRate.GBP -> R.drawable.ic_uk_flag
                                is ExchangeRate.USD -> R.drawable.ic_us_flag
                            }
                        ),
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        tint = Color.Unspecified,
                        contentDescription = "Swap between currencies"
                    )
                    Text(
                        text = when (exchangeRate) {
                            is ExchangeRate.EUR -> "1 ETH = ${exchangeRate.value}€"
                            is ExchangeRate.GBP -> "1 ETH = ${exchangeRate.value}£"
                            is ExchangeRate.USD -> "1 ETH = ${exchangeRate.value}$"
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun InputFields(viewModel: IMainViewModel) {
    val cash = viewModel.currentTransferValueInCash.collectAsState()
    val crypto = viewModel.currentTransferValueInEth.collectAsState()
    val isCashMode = viewModel.isTransferInCash.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = if (isCashMode.value) cash.value.toString() else crypto.value.toString(),
            onValueChange = {
                if (isCashMode.value)
                    viewModel.setNewValueCash(it.toFloat())
                else
                    viewModel.setNewValueETH(it.toFloat())
            },
            modifier = Modifier.align(CenterHorizontally),
            placeholder = { Text("Enter Transfer Value") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(
            text = if (isCashMode.value) crypto.value.toString() else cash.value.toString(),
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(start = 12.dp)
                .fillMaxWidth(),
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            color = Color.DarkGray
        )
    }
}

@Composable
fun SwapButton(viewModel: IMainViewModel) {
    Box(Modifier.fillMaxWidth()) {
        IconButton(
            onClick = {
                viewModel.swapTransferMode()
            },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-20).dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_swap_vertical_circle_24),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .border(8.dp, Color.White, CircleShape),
                tint = Color.White,
                contentDescription = "Swap between using ETH or selected Currency"
            )
        }
    }
}

@Composable
fun CurrencyButton(viewModel: IMainViewModel) {
    val exchangeRate = viewModel.currentExchangeRate.collectAsState()
    Box(Modifier.fillMaxWidth()) {
        IconButton(
            onClick = { viewModel.setIsCurrencySelectorOpen(true) },
            modifier = Modifier
                .align(Alignment.CenterEnd)
        ) {
            Icon(
                painter = painterResource(
                    id = when (exchangeRate.value) {
                        is ExchangeRate.EUR -> R.drawable.ic_eu_flag
                        is ExchangeRate.GBP -> R.drawable.ic_uk_flag
                        is ExchangeRate.USD -> R.drawable.ic_us_flag
                    }
                ),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                tint = Color.Unspecified,
                contentDescription = "Swap between currencies"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CryptoScreenAppTheme {
        MainScreen(FakeViewModel)
    }
}