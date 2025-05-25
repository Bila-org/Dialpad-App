package com.example.dialpad

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DialpadViewModel : ViewModel() {

    private val _phoneNumber = MutableStateFlow(TextFieldValue(""))

    val phoneNumber: StateFlow<TextFieldValue> = _phoneNumber.asStateFlow()



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


    fun addDigit(digit: String) {
        viewModelScope.launch {
            toneGenerator?.startTone(toneMap[digit] ?: return@launch, 120)

            val current = _phoneNumber.value
            val newText = current.text.substring(0, current.selection.start) +
                    digit +
                    current.text.substring(current.selection.end)
            _phoneNumber.value = current.copy(
                text = newText,
                selection = androidx.compose.ui.text.TextRange(current.selection.start + 1)
            )
        }
    }


    fun removeDigit() {
        viewModelScope.launch {
            val current = _phoneNumber.value
            if (current.selection.start > 0) {
                val newText = current.text.substring(0, current.selection.start - 1) +
                        current.text.substring(current.selection.end)
                _phoneNumber.value = current.copy(
                    text = newText,
                    selection = androidx.compose.ui.text.TextRange(current.selection.start - 1)
                )
            }
        }
    }


    fun clearNumber() {
        //_phoneNumber.update { "" }
        _phoneNumber.value = TextFieldValue("")
    }


    fun updateTextFieldValue(newValue: TextFieldValue) {
        _phoneNumber.value = newValue
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