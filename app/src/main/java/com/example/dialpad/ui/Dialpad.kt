package com.example.dialpad.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
    onNumberPress: (String) -> Unit,
    onNumberRelease: () -> Unit,
    onContactsClicked: () -> Unit,
    onCallClicked: () -> Unit,
    onBackspaceClicked: () -> Unit,
    onBackspaceLongClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val interactionSource = remember { MutableInteractionSource() }

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
                onPress = { onNumberPress(number) },
                onRelease = { onNumberRelease() },
                onLongPress = {
                    if (number == "0") {
                        onBackspaceClicked()
                        onNumberPress("+")
                    } else null
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        item() {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(radius = 30.dp),
                            onClick = onContactsClicked
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Contacts,
                        contentDescription = "Contacts",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black.copy(alpha = 0.6f)
                    )
                }
                //  Spacer(modifier = Modifier.height(4.dp))
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
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF008400).copy(alpha = 0.6f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(
                                bounded = true,
                                radius = 30.dp,
                                // color = Color.White
                            ),
                            onClick = onCallClicked
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Continue",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
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
            Box(
                modifier = Modifier
                    .size(60.dp)  // Larger than the IconButton
                    //   .padding(5.dp)
                    .indication(
                        interactionSource = interactionSource,
                        indication = rememberRipple(radius = 30.dp)
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { offset ->
                                val press = PressInteraction.Press(offset)
                                interactionSource.tryEmit(press)
                                try {
                                    awaitRelease()
                                } finally {
                                    interactionSource.tryEmit(PressInteraction.Release(press))
                                }
                            },
                            onLongPress = { onBackspaceLongClicked() },
                            onTap = { onBackspaceClicked() }
                        )
                    },
                contentAlignment = Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Backspace,
                    contentDescription = "Backspace",
                    tint = Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

}


@Composable
fun DialpadButton(
    number: String,
    letters: String,
    onPress: () -> Unit,
    onRelease: () -> Unit,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    val ripple = rememberRipple(
        bounded = true,
        radius = 35.dp,
        color = Color(0xFF6200EE)
        //MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f)
    )

    Surface(
        shape = CircleShape,
        //color = Color(0xFF6200EE),
        color = Color.Transparent,
        modifier = Modifier
            .size(72.dp)
            .indication(
                interactionSource = interactionSource,
                indication = ripple
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        val press = PressInteraction.Press(offset)
                        interactionSource.tryEmit(press)
                        onPress()

                        try {
                            awaitRelease()
                        } finally {
                            interactionSource.tryEmit(PressInteraction.Release(press))
                            onRelease()
                        }
                    },
                    onLongPress = {
                        onLongPress()
                    }
                )
            }
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
            onNumberPress = {},
            onNumberRelease = {},
            onContactsClicked = {},
            onCallClicked = {},
            onBackspaceClicked = {},
            onBackspaceLongClicked = {},
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
            onPress = {},
            onRelease = {},
            onLongPress = {}
        )
    }
}
