package com.example.dialpad.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.dialpad.R
import com.example.dialpad.ui.theme.DialpadTheme


val dialPadItems = listOf(
    "1" to "",
    "2" to "ABC",
    "3" to "DEF",
    "4" to "GHI",
    "5" to "JKL",
    "6" to "MNO",
    "7" to "PQRS",
    "8" to "TUV",
    "9" to "WXYZ",
    "*" to "",
    "0" to "+",
    "#" to ""
)


@Composable
fun Dialpad(
    onNumberPress: (String) -> Unit,
    onNumberRelease: () -> Unit,
    onContactsClick: () -> Unit,
    onCallClick: () -> Unit,
    onBackspaceClick: () -> Unit,
    onBackspaceLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(R.dimen.large_padding),
            vertical = dimensionResource(R.dimen.medium_padding)
        ),
        horizontalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.very_small_padding), Alignment.CenterHorizontally
        ),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.very_small_padding)
        )
    ) {
        items(dialPadItems.size) { index ->
            val (number, letters) = dialPadItems[index]
            DialpadButton(
                number = number,
                letters = letters,
                onPress = { onNumberPress(number) },
                onRelease = { onNumberRelease() },
                onLongPress = {
                    if (number == "0") {
                        onBackspaceClick()
                        onNumberPress("+")
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        item {
            DialpadIconButton(
                icon = Icons.Outlined.Contacts,
                contentDescription = "Contacts",
                onClick = onContactsClick,
                tint = Color.Black.copy(alpha = 0.6f),
                backgroundColor = Color.Transparent,
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.medium_padding))
            )
        }

        item {
            DialpadIconButton(
                icon = Icons.Filled.Call,
                contentDescription = "Call",
                onClick = onCallClick,
                tint = Color.White,
                backgroundColor = Color(0xFF008400).copy(alpha = 0.6f),
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.medium_padding))
            )
        }

        item {
            DialpadIconButton(
                icon = Icons.AutoMirrored.Outlined.Backspace,
                contentDescription = "Backspace",
                onClick = onBackspaceClick,
                onLongClick = onBackspaceLongClick,
                tint = Color.Black.copy(alpha = 0.6f),
                backgroundColor = Color.Transparent,
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.medium_padding))
            )
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun DialpadPreview() {
    DialpadTheme() {
        Dialpad(
            onNumberPress = {},
            onNumberRelease = {},
            onContactsClick = {},
            onCallClick = {},
            onBackspaceClick = {},
            onBackspaceLongClick = {},
            modifier = Modifier.systemBarsPadding()
        )
    }
}