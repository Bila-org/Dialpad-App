package com.example.dialpad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.dialpad.ui.DialpadScreen
import com.example.dialpad.ui.theme.DialpadTheme

class MainActivity : ComponentActivity() {

    private val viewModel: DialpadViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DialpadTheme(darkTheme = false, dynamicColor = false) {
                DialpadScreen(
                    phoneNumber = viewModel.phoneNumber.collectAsState().value,
                    onNumberClicked = {digit ->
                        viewModel.addDigit(digit)
                    },
                    onContactsClicked = {},
                    onContinueClicked = {},
                    onBackspaceClicked = {
                        viewModel.removeDigit()
                    }
                )
            }
        }
    }
}



