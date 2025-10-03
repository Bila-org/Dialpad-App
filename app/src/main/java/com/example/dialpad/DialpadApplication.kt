package com.example.dialpad

import android.app.Application
import com.example.dialpad.data.ContactsRepositoryImpl
import com.example.dialpad.domain.ContactsRepository


class DialpadApplication : Application() {

    val contactsRepository: ContactsRepository by lazy {
        ContactsRepositoryImpl(contentResolver)
    }
}