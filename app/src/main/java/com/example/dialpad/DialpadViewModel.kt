package com.example.dialpad

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DialpadViewModel: ViewModel() {

    private val _phoneNumber = MutableStateFlow<String>("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()


    fun addDigit(digit: String){
        _phoneNumber.update {
                it + digit
        }
    }

    fun removeDigit(){
        _phoneNumber.update {
            if(it.isNotEmpty())
                it.dropLast(1)
            else it
        }
    }

    fun clearNumber(){
        _phoneNumber.update { "" }
    }
}