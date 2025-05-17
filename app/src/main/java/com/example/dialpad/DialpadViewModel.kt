package com.example.dialpad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class DialpadViewModel : ViewModel() {

    private val _phoneNumber = MutableStateFlow<String>("")

    //val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()
    val phoneNumber: StateFlow<String> = _phoneNumber.map { digits ->
        formatPhoneNumber(digits)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, "")


    fun addDigit(digit: String) {
        if (_phoneNumber.value.length < 15) {
            _phoneNumber.update {
                //val newNumber =
                it + digit
                //formatPhoneNumber(newNumber)
            }
        }
    }

    fun removeDigit() {
        _phoneNumber.update {
            if (it.isNotEmpty()) {
                //val newNumber =
                it.dropLast(1)
                //formatPhoneNumber(newNumber)
            } else it
        }
    }

    fun clearNumber() {
        _phoneNumber.update { "" }
    }

    private fun formatPhoneNumber(number: String): String {

        return when {
                number.length <= 3 -> number
                else -> buildString {
                    append("(${number.take(3)}) ")

                    val remaining = number.drop(3)
                    val chunked = remaining.chunked(3)

                    chunked.forEachIndexed { index, chunk ->
                        append(chunk)
                        if (index != chunked.lastIndex) {
                            append("-")
                        }
                    }
                }.trim()
            }
    }
}