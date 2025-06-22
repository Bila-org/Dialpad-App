package com.example.dialpad

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.content.ContextCompat
import com.example.dialpad.ui.DialpadScreen
import com.example.dialpad.ui.theme.DialpadTheme

class MainActivity : ComponentActivity() {

    private val viewModel: DialpadViewModel by viewModels()

    // Permission launcher for CALL_PHONE
    private val callPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            makeCall(viewModel.phoneNumber.value.text)
        } else {
            Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show()
        }
    }


    private val contactsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            launchContactsPicker()
        } else {
            Toast.makeText(this, "Contacts permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private val contactLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { contactUri ->
                retrievePhoneNumber(contactUri)
            }
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
            Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchContactsPicker() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        contactLauncher.launch(intent)
    }


    private fun retrievePhoneNumber(contactUri: Uri) {
        try {
            val cursor = this.contentResolver.query(
                contactUri,
                arrayOf(ContactsContract.Contacts._ID),
                null,
                null,
                null
            )
            cursor?.use {
                if (it.moveToFirst()) {
                    val contactId = it.getColumnIndex(ContactsContract.Contacts._ID)
                    if (contactId != -1) {
                        val contactIdValue = it.getString(contactId)
                        val phoneCursor = this.contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                            arrayOf(contactIdValue),
                            null
                        )
                        phoneCursor?.use { phone ->
                            if (phone.moveToFirst()) {
                                val phoneNumberIndex =
                                    phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                if (phoneNumberIndex != -1) {
                                    val phoneNumber =
                                        TextFieldValue(phone.getString(phoneNumberIndex) ?: "")
                                    viewModel.updateTextFieldValue(phoneNumber)
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error retrieving phone number", Toast.LENGTH_SHORT).show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.ECLAIR)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DialpadTheme(darkTheme = false, dynamicColor = false) {
                DialpadScreen(
                    phoneNumber = viewModel.phoneNumber.collectAsState().value,
                    onTextFieldValueChange = {
                        viewModel.updateTextFieldValue(it)
                    },
                    onNumberPress = { digit ->
                        viewModel.startTone(digit)
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
                    }
                )
            }
        }
    }

}