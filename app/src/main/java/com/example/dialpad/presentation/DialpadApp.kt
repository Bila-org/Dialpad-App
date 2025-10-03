package com.example.dialpad.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dialpad.presentation.util.DialpadAlertDialog
import com.example.dialpad.presentation.util.DialpadTopAppbar
import com.example.dialpad.presentation.util.PhoneNumberField
import com.example.dialpad.ui.theme.DialpadTheme


@Composable
fun DialpadApp(
    state: DialpadState,
    onTextFieldValueChange: (TextFieldValue) -> Unit,
    onNumberPress: (String) -> Unit,
    onNumberRelease: () -> Unit,
    onContactsClick: () -> Unit,
    onCallClick: (TextFieldValue) -> Unit,
    onBackspaceClick: () -> Unit,
    onBackspaceLongClick: () -> Unit,
    isContactListNumberSelected: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val phoneNumber = rememberUpdatedState(state.phoneNumber)
    val showContactSectionBox = state.showContactSelectionBox

    val message = state.errorMessage
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        if (message != null)
            snackbarHostState
                .showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
    }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            DialpadTopAppbar(
                onBackClick = { onBackClick() }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }

    ) { innerPadding ->

        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
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

            PhoneNumberField(
                value = phoneNumber.value,
                onValueChange = onTextFieldValueChange,
            )

            Spacer(modifier = Modifier.weight(1f))

            Dialpad(
                onNumberPress = onNumberPress,
                onNumberRelease = onNumberRelease,
                onContactsClick = onContactsClick,
                onCallClick = {
                    onCallClick(phoneNumber.value)
                },
                onBackspaceClick = onBackspaceClick,
                onBackspaceLongClick = onBackspaceLongClick,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }

    if (showContactSectionBox) {
        DialpadAlertDialog(
            onDismiss = { isContactListNumberSelected(false) },
            onConfirm = { isContactListNumberSelected(true) }
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun DialpadScreenPreview() {
    DialpadTheme {
        DialpadApp(
            state = DialpadState(),
            onTextFieldValueChange = {},
            onNumberPress = {},
            onNumberRelease = {},
            onContactsClick = {},
            onCallClick = {},
            onBackspaceClick = {},
            onBackspaceLongClick = {},
            isContactListNumberSelected = {},
            onBackClick = {}
        )
    }
}