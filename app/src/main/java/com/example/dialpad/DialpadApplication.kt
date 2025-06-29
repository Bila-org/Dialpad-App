package com.example.dialpad

import android.app.Application
import com.example.dialpad.data.ContactsRepository

class DialpadApplication : Application() {

    val contactsRepository: ContactsRepository by lazy {
        ContactsRepository(contentResolver)
    }
}