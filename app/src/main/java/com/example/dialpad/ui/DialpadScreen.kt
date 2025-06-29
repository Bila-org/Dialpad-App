package com.example.dialpad.ui


import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dialpad.model.DialpadState
import com.example.dialpad.ui.theme.DialpadTheme
import com.example.dialpad.ui.util.PhoneNumberVisualTransformation

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun DialpadScreenPreview() {
    DialpadTheme {
        DialpadScreen(
            state = DialpadState(),
            onTextFieldValueChange = {},
            onNumberPress = {},
            onNumberRelease = {},
            onContactsClicked = {},
            onCallClicked = {},
            onBackspaceClicked = {},
            onBackspaceLongClicked = {},
            isContactListNumberSelected = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialpadScreen(
    state: DialpadState,
    onTextFieldValueChange: (TextFieldValue) -> Unit,
    onNumberPress: (String) -> Unit,
    onNumberRelease: () -> Unit,
    onContactsClicked: () -> Unit,
    onCallClicked: (TextFieldValue) -> Unit,
    onBackspaceClicked: () -> Unit,
    onBackspaceLongClicked: () -> Unit,
    isContactListNumberSelected: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val layoutDirection = LocalLayoutDirection.current
    val phoneNumber = state.phoneNumber
    val showContactSectionBox = state.showContactSelectionBox


    Scaffold(
        contentWindowInsets = WindowInsets(
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
            left = WindowInsets.systemBars.asPaddingValues().calculateLeftPadding(layoutDirection),
            right = WindowInsets.systemBars.asPaddingValues().calculateRightPadding(layoutDirection)
        ),
//         contentColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
//                colors = TopAppBarDefaults.topAppBarColors(
//                  MaterialTheme.colorScheme.outline
//                ) ,
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("PhoneNumberField"),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    singleLine = true,
                    visualTransformation = PhoneNumberVisualTransformation(),
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

    if (showContactSectionBox) {
        AlertDialog(
            onDismissRequest = { isContactListNumberSelected(false) },
            title = {
                Text("Replace Phone Number ?")
            },
            text = {
                Text("Do you want to replace the current phoneNumber with the selected one from contact list?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        isContactListNumberSelected(true)
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isContactListNumberSelected(false)
                    }
                ) {
                    Text("No")
                }
            },
            modifier = Modifier
        )
    }
}
