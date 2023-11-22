@file:OptIn(ExperimentalMaterial3Api::class)

package com.iulian.iancu.cryptoscreenapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iulian.iancu.cryptoscreenapp.ui.theme.CryptoScreenAppTheme
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
    var text by remember {
        mutableStateOf("Hello")
    }
    Box(contentAlignment = Alignment.Center) {
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.Center),
            placeholder = { Text("Enter text") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )

        // Round button overlapping the left side of the TextField
        IconButton(
            onClick = { /* TODO: Handle click */ },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-20).dp), // Adjust the offset as needed
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

        // Third button on the right side of the TextField
        Button(
            onClick = { /* TODO: Handle click */ },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (20).dp), // Adjust the offset as needed
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("Max", color = Color.White)
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