package com.example.dialpad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
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
                    onTextFieldValueChange = {
                        viewModel.updateTextFieldValue(it)
                    },
                    onNumberClicked = { digit ->
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



