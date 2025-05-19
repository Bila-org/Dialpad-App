package com.example.dialpad

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DialpadViewModel : ViewModel() {

    private val _phoneNumber = MutableStateFlow<String>("")

    //val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()
    val phoneNumber: StateFlow<String> = _phoneNumber.map { digits ->
        formatPhoneNumber(digits)
    }.stateIn(viewModelScope, SharingStarted.Lazily, "")

    private var toneGenerator: ToneGenerator? = ToneGenerator(AudioManager.STREAM_DTMF, 80)
    private val toneMap = mapOf(
        "0" to ToneGenerator.TONE_DTMF_0,
        "1" to ToneGenerator.TONE_DTMF_1,
        "2" to ToneGenerator.TONE_DTMF_2,
        "3" to ToneGenerator.TONE_DTMF_3,
        "4" to ToneGenerator.TONE_DTMF_4,
        "5" to ToneGenerator.TONE_DTMF_5,
        "6" to ToneGenerator.TONE_DTMF_6,
        "7" to ToneGenerator.TONE_DTMF_7,
        "8" to ToneGenerator.TONE_DTMF_8,
        "9" to ToneGenerator.TONE_DTMF_9,
        "*" to ToneGenerator.TONE_DTMF_S,
        "#" to ToneGenerator.TONE_DTMF_P
    )

    fun playTone(key: String){
        viewModelScope.launch {
            toneGenerator?.startTone(toneMap[key]?: return@launch, 150)
        }
    }

    fun addDigit(digit: String) {
        playTone(digit)
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
        if (number.length <= 3) return number

        val counterCode = "(${number.take(3)}) "
        val remaining = number.drop(3)

        return counterCode + remaining.chunked(3).joinToString(
            "-"
        )
    }

    // Release ToneGenerator resources when ViewModel is cleared
    override fun onCleared() {
        toneGenerator?.release()
        toneGenerator = null
        super.onCleared()
    }

}