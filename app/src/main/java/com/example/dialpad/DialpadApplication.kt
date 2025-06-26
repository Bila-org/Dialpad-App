package com.example.dialpad

import android.app.Application

class DialpadApplication : Application() {

    val contactsRepository: ContactsRepository by lazy {
        ContactsRepository(contentResolver)
    }
}