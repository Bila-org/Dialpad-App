package com.example.dialpad.data

import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract
import com.example.dialpad.domain.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ContactsRepositoryImpl(
    private val contentResolver: ContentResolver
) : ContactsRepository {

    override suspend fun getPhoneNumber(contactUri: Uri): ContactResult =
        withContext(Dispatchers.IO) {
            try {
                val contactId = getContactIdFromUri(contactUri)
                    ?: return@withContext ContactResult.NoNumberFound

                return@withContext getPhoneNumberFromContactId(contactId)
            } catch (e: SecurityException) {
                return@withContext ContactResult.Error("Missing READ_CONTACTS permission")
            } catch (e: Exception) {
                return@withContext ContactResult.Error("Failed to fetch phone number: ${e.message}")
            }
        }

    private fun getContactIdFromUri(contactUri: Uri): String? {
        return contentResolver.query(
            contactUri,
            arrayOf(ContactsContract.Contacts._ID),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                if (contactIdIndex != -1) {
                    cursor.getString(contactIdIndex)
                } else {
                    null
                }
            } else {
                null
            }
        }
    }


    private fun getPhoneNumberFromContactId(contactId: String): ContactResult {
        return contentResolver.query(
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
                    val phoneNumber = phoneCursor.getString(phoneNumberIndex)
                    if (!phoneNumber.isNullOrBlank()) {
                        ContactResult.Success(phoneNumber)
                    } else {
                        ContactResult.NoNumberFound
                    }
                } else {
                    ContactResult.NoNumberFound
                }
            } else {
                ContactResult.NoNumberFound
            }
        }?: ContactResult.NoNumberFound
    }
}

sealed class ContactResult {
    data class Success(val phoneNumber: String) : ContactResult()
    object NoNumberFound : ContactResult()
    data class Error(val message: String) : ContactResult()
}
