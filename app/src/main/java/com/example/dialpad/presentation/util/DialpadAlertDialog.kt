package com.example.dialpad.presentation.util

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.ContentPasteSearch
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dialpad.ui.theme.DialpadTheme


@Composable
fun DialpadAlertDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        modifier = modifier,
        icon = {
          Icon(
              imageVector = Icons.Default.Contacts,
              contentDescription = "Warning Icon",
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(40.dp)
          )
        },
        title = {
            Text(
                text = "Replace Phone Number?",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
        },
        text = {
            Text(
                text ="Do you want to replace the current Phone Number with the selected one from contact list?",
                style = MaterialTheme.typography.bodyLarge
                )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }) {
                Text(
                    text = "Replace",
                    color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }) {
                Text(
                    text = "Cancel",
                    )
            }
        },
        shape = MaterialTheme.shapes.extraLarge,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        tonalElevation = AlertDialogDefaults.TonalElevation
    )
}


@Preview(
    showBackground = true,
)
@Composable
fun DialpadAlertDialogPreview() {
    DialpadTheme {
        DialpadAlertDialog(
            onDismiss = {},
            onConfirm = {}
        )
    }
}
