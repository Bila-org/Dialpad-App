package com.example.dialpad

import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract

class ContactsRepository(private val contentResolver: ContentResolver) {

    fun getPhoneNumber(contactUri: Uri): String? {
        try {
            contentResolver.query(
                contactUri,
                arrayOf(ContactsContract.Contacts._ID),
                null,
                null,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                    if (contactIdIndex != -1) {
                        val contactId = cursor.getString(contactIdIndex)
                        contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                            arrayOf(contactId),
                            null
                        )?.use { phoneCursor ->
                            if (phoneCursor.moveToFirst()) {
                                val phoneNumberIndex =
                                    phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                if (phoneNumberIndex != -1) {
                                    return phoneCursor.getString(phoneNumberIndex)
                                    //TextFieldValue(phoneCursor.getString(phoneNumberIndex) ?: "")
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            //   Toast.makeText(this, "Error retrieving phone number", Toast.LENGTH_SHORT).show()
            return null
        }
        return null
    }
}