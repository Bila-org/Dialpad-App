package com.example.dialpad.presentation.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.dialpad.ui.theme.DialpadTheme

@Composable
fun PhoneNumberField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
) {
    CompositionLocalProvider(
        LocalTextInputService provides null
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
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
}


@Preview(
    showBackground = true,
)
@Composable
fun PhoneNumberFieldPreview() {
    DialpadTheme {
        PhoneNumberField(
            value = TextFieldValue("123456789"),
            onValueChange = {}
        )
    }
}
