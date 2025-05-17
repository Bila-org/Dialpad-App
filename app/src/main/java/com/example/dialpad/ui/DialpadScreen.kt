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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
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
        DialpadScreen(
            phoneNumber = "",
            onNumberClicked = {},
            onContactsClicked = {},
            onContinueClicked = {},
            onBackspaceClicked = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialpadScreen(
    phoneNumber: String,
    onNumberClicked: (String) -> Unit,
    onContactsClicked: () -> Unit,
    onContinueClicked: () -> Unit,
    onBackspaceClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val layoutDirection = LocalLayoutDirection.current
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
                            text = "Audio Call",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Cellular Network",
                            fontSize = 13.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Move Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Adjust Setting"
                        )
                    }
                }
            )
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
                    width = 1.dp,
                    color = Color(0xFF03A9F4)
                ),
            ) {
                Icon(
                    imageVector = Icons.Default.SyncAlt,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Tucson Test",
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = phoneNumber.ifEmpty { "Enter number" },
                color = if (phoneNumber.isEmpty()) Color.Gray else Color.Black,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Dialpad(
                onNumberClicked = onNumberClicked,
                onContactsClicked = onContactsClicked,
                onContinueClicked = onContinueClicked,
                onBackspaceClicked = onBackspaceClicked,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}
