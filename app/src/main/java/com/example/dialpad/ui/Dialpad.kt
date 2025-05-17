package com.example.dialpad.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    onNumberClicked: (String) -> Unit,
    onContactsClicked: () -> Unit,
    onContinueClicked: () -> Unit,
    onBackspaceClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
          modifier = modifier,
        //    .statusBarsPadding(),
        contentPadding = PaddingValues(horizontal = 30.dp),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center
    ) {
        items(dialPadItems.size) { index ->
            val (number, letters) = dialPadItems[index]
            DialpadButton(
                number = number,
                letters = letters,
                onButtonClick = { onNumberClicked(number) },
                modifier = Modifier.fillMaxSize()
            )
        }

        item() {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = onContactsClicked,
                    modifier = Modifier
                        .size(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Contacts,
                        contentDescription = "Contacts",
                        modifier = Modifier.fillMaxSize(0.4f),
                        tint = Color.Black.copy(alpha = 0.6f)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Contacts",
                    fontSize = 10.sp,
                    color = Color.Black.copy(alpha = 0.6f)
                )
            }
        }

        item() {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onContinueClicked,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF008400).copy(alpha = 0.6f),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .padding(0.dp)
                        .size(50.dp),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Continue",
                        modifier = Modifier
                            .size(50.dp)
                            .wrapContentSize()
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Continue",
                    fontSize = 10.sp,
                    color = Color.Black.copy(alpha = 0.6f)
                )
            }
        }

        item() {
            IconButton(
                onClick = onBackspaceClicked,
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Backspace,
                    contentDescription = "Contacts",
                    tint = Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxSize(0.4f)
                )
            }
        }
    }

}

@Composable
fun DialpadButton(
    number: String,
    letters: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onButtonClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        ),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 4.dp
        ),
        modifier = modifier
        //  .padding(4.dp)
        //   .size(55.dp)

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
            //   .background(Color.Gray)
        ) {
            Text(
                text = number,
                fontSize = 28.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            Text(
                text = letters,
                fontSize = 8.sp,
                color = Color.Black.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
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
            onNumberClicked = {},
            onContactsClicked = {},
            onContinueClicked = {},
            onBackspaceClicked = {},
            modifier = Modifier.systemBarsPadding()
        )
    }
}


@Preview(
    showBackground = true,
    showSystemUi = false,
)
@Composable
fun DialpadButtonPreview() {
    DialpadTheme {
        DialpadButton(
            number = "1",
            letters = "ABC",
            onButtonClick = {}
        )
    }
}
