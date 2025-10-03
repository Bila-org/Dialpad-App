package com.example.dialpad.presentation.util

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.dialpad.ui.theme.DialpadTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialpadTopAppbar(
    onBackClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Column() {
                Text(
                    text = "Audio Call", fontSize = 20.sp, fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Cellular Network", fontSize = 13.sp
                )
            }
        }, navigationIcon = {
            IconButton(onClick = { onBackClick()}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Move Back"
                )
            }
        }, actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Tune, contentDescription = "Adjust Setting"
                )
            }
        }
    )
}


@Preview(
    showBackground = true,
)
@Composable
fun DialpadTopAppbarPreview() {
    DialpadTheme {
        DialpadTopAppbar()
    }
}