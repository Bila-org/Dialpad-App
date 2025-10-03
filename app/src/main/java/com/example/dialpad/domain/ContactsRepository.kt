package com.example.dialpad.domain

import android.net.Uri
import com.example.dialpad.data.ContactResult

interface ContactsRepository {
    suspend fun getPhoneNumber(contactUri: Uri): ContactResult
}