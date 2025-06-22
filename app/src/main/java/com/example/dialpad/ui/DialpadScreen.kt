package com.example.dialpad.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dialpad.ui.theme.DialpadTheme

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun DialpadScreenPreview() {
    DialpadTheme {
        DialpadScreen(phoneNumber = TextFieldValue(""),
            onTextFieldValueChange = {},
            onNumberPress = {},
            onNumberRelease = {},
            onContactsClicked = {},
            onCallClicked = {},
            onBackspaceClicked = {},
            onBackspaceLongClicked = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DialpadScreen(
    phoneNumber: TextFieldValue,
    onTextFieldValueChange: (TextFieldValue) -> Unit,
    onNumberPress: (String) -> Unit,
    onNumberRelease: () -> Unit,
    onContactsClicked: () -> Unit,
    onCallClicked: (TextFieldValue) -> Unit,
    onBackspaceClicked: () -> Unit,
    onBackspaceLongClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val interactionSource = remember { MutableInteractionSource() }
    val layoutDirection = LocalLayoutDirection.current
    val focusRequester = remember { FocusRequester() }



    Scaffold(
        contentWindowInsets = WindowInsets(
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
            left = WindowInsets.systemBars.asPaddingValues().calculateLeftPadding(layoutDirection),
            right = WindowInsets.systemBars.asPaddingValues().calculateRightPadding(layoutDirection)
        ),
        // contentColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                //colors = TopAppBarDefaults.topAppBarColors(
                //  MaterialTheme.colorScheme.outline
                //) ,
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
                    IconButton(onClick = { }) {
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
                })
        },

        ) { innerPadding ->

        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalDivider(
                // Adds a thin line below TopAppBar
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF03A9F4)
                ),
                contentPadding = PaddingValues(8.dp),
                border = BorderStroke(
                    width = 1.dp, color = Color(0xFF03A9F4)
                ),
            ) {
                Icon(
                    imageVector = Icons.Default.SyncAlt, contentDescription = null
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Tucson Test", fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            CompositionLocalProvider(
                LocalTextInputService provides null
            ) {
                BasicTextField(
                    value = phoneNumber,
                    onValueChange = onTextFieldValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    /*
                        .focusRequester(focusRequester)
                        // Todo: Check why the clickable and interaction source is needed here
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                       ) {
                            focusRequester.requestFocus()   // Focus text field on tap
                        },
                        */
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    //readOnly = true,
                    singleLine = true,
                    visualTransformation = PhoneNumberVisualTransformation(),
                    //visualTransformation = PhoneNumberTransformation(),
//                    interactionSource = interactionSource,
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 36.sp, textAlign = TextAlign.Center
                    )
                )
            }


            Spacer(modifier = Modifier.weight(1f))

            Dialpad(
                onNumberPress = onNumberPress,
                onNumberRelease = onNumberRelease,
                onContactsClicked = onContactsClicked,
                onCallClicked = {
                    onCallClicked(phoneNumber)
                },
                onBackspaceClicked = onBackspaceClicked,
                onBackspaceLongClicked = onBackspaceLongClicked,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}


class USPhoneNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.filter { it.isDigit() }

        val formatted = when {
            trimmed.length <= 3 -> trimmed
            trimmed.length <= 6 -> "(${trimmed.substring(0, 3)}) ${trimmed.substring(3)}"
            trimmed.length <= 10 -> "(${trimmed.substring(0, 3)}) ${
                trimmed.substring(
                    3, 6
                )
            }-${trimmed.substring(6)}"

            else -> "(${trimmed.substring(0, 3)}) ${trimmed.substring(3, 6)}-${
                trimmed.substring(
                    6, 10
                )
            }${trimmed.substring(10)}"
        }


        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val digitsOnly = text.text.filter { it.isDigit() }.take(offset).length
                return when {
                    digitsOnly <= 3 -> digitsOnly
                    digitsOnly <= 6 -> digitsOnly + 2
                    digitsOnly <= 10 -> digitsOnly + 4
                    else -> digitsOnly + 4
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                val chars = formatted.take(offset)
                var digitCount = 0
                var i = 0
                while (i < offset && i < chars.length) {
                    if (chars[i].isDigit()) digitCount++
                    i++
                    if (i == offset - 1 && !formatted[i].isDigit()) {
                        return digitCount
                    }
                }
                return digitCount
            }
        }

        return TransformedText(
            text = AnnotatedString(formatted), offsetMapping = offsetMapping
        )
    }
}