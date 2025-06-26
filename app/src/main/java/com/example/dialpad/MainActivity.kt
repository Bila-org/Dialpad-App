package com.example.dialpad

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import com.example.dialpad.ui.DialpadScreen
import com.example.dialpad.ui.theme.DialpadTheme

class MainActivity : ComponentActivity() {

    private val viewModel: DialpadViewModel by viewModels { DialpadViewModel.Factory }

    // Permission launcher for CALL_PHONE
    private val callPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            makeCall(viewModel.dialpadState.value.phoneNumber.text)
        } else {
            //  coroutineScope.launch{
            //    snackbarHostState.showSnackbar("Call permission denied")
            // }

            //Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun makeCall(phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel: $phoneNumber")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        } catch (e: SecurityException) {
            // Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show()
            //coroutineScope.launch{
            //    snackbarHostState.showSnackbar("Call permission denied")
            //}
        }
    }


    private val contactsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            launchContactsPicker()
        } else {
            //  Toast.makeText(this, "Contacts permission denied", Toast.LENGTH_SHORT).show()
            //coroutineScope.launch{
            //    snackbarHostState.showSnackbar("Contacts permission denied")
            //}
        }
    }

    private val contactLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { contactUri ->
                viewModel.retrievePhoneNumber(contactUri)
            }
        }
    }

    private fun launchContactsPicker() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        contactLauncher.launch(intent)
    }


    @RequiresApi(Build.VERSION_CODES.ECLAIR)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DialpadTheme(darkTheme = false, dynamicColor = false) {
                DialpadScreen(
                    state = viewModel.dialpadState.collectAsState().value,
                    onTextFieldValueChange = {
                        viewModel.updateTextFieldValue(it)
                    },
                    onNumberPress = { digit ->
                        viewModel.addDigit(digit)
                    },
                    onNumberRelease = {
                        viewModel.stopTOne()
                    },
                    onContactsClicked = {
                        if (ContextCompat.checkSelfPermission(
                                this,
                                Manifest.permission.READ_CONTACTS
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            launchContactsPicker()
                        } else {
                            contactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                        }
                    },
                    onCallClicked = { phoneNumber ->
                        if (ContextCompat.checkSelfPermission(
                                this,
                                Manifest.permission.CALL_PHONE
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            makeCall(phoneNumber.text)
                        } else {
                            callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                        }
                    },
                    onBackspaceClicked = {
                        viewModel.removeDigit()
                    },
                    onBackspaceLongClicked = {
                        viewModel.clearNumber()
                    },
                    isContactListNumberSelected = { isContactListNumberSelected ->
                        viewModel.dialOrContactPhoneNumber(isContactListNumberSelected)
                    }
                )
            }
        }
    }

}