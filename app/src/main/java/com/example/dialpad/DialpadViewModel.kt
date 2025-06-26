package com.example.dialpad

import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class DialpadState(
    val phoneNumber: TextFieldValue = TextFieldValue(""),
    val showContactSelectionBox: Boolean = false,
    val errorMessage: String? = null
)


class DialpadViewModel(private val contactsRepository: ContactsRepository) : ViewModel() {


    private val _dialpadState = MutableStateFlow<DialpadState>(DialpadState())
    val dialpadState: StateFlow<DialpadState> = _dialpadState.asStateFlow()


//    private val _phoneNumber = MutableStateFlow(TextFieldValue(""))
//    val phoneNumber: StateFlow<TextFieldValue> = _phoneNumber.asStateFlow()


    private var toneGenerator: ToneGenerator? = ToneGenerator(AudioManager.STREAM_DTMF, 80)
    private var phoneNumberFromContactList: TextFieldValue = TextFieldValue("")

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


    private fun startTone(digit: String) {
        viewModelScope.launch {
            toneGenerator?.startTone(toneMap[digit] ?: return@launch, -1)
        }
    }


    fun stopTOne() {
        toneGenerator?.stopTone()
    }


    fun addDigit(digit: String) {
        startTone(digit)
        val current = _dialpadState.value.phoneNumber
        val newText = current.text.substring(0, current.selection.start) +
                digit +
                current.text.substring(current.selection.end)
        _dialpadState.update {
            it.copy(
                phoneNumber = TextFieldValue(
                    text = newText,
                    selection = androidx.compose.ui.text.TextRange(current.selection.start + 1)
                )
            )
        }
    }


    fun removeDigit() {
        viewModelScope.launch {
            val current = _dialpadState.value.phoneNumber
            if (current.selection.start > 0) {
                val newText = current.text.substring(0, current.selection.start - 1) +
                        current.text.substring(current.selection.end)
                _dialpadState.update {
                    it.copy(
                        phoneNumber = TextFieldValue(
                            text = newText,
                            selection = androidx.compose.ui.text.TextRange(current.selection.start - 1)
                        )
                    )
                }
            }
        }
    }


    fun clearNumber() {
        _dialpadState.update {
            it.copy(
                phoneNumber = TextFieldValue("")
            )
        }
    }


    fun updateTextFieldValue(newValue: TextFieldValue) {
        _dialpadState.update {
            it.copy(
                phoneNumber = newValue
            )
        }
    }

    fun dialOrContactPhoneNumber(isContactListNumberSelected: Boolean) {
        _dialpadState.update {
            it.copy(
                showContactSelectionBox = false
            )
        }
        if (isContactListNumberSelected) {
            _dialpadState.update {
                it.copy(
                    phoneNumber = phoneNumberFromContactList
                )
            }
        }
    }


    fun retrievePhoneNumber(contactUri: Uri) {
        val contact = contactsRepository.getPhoneNumber(contactUri)
        if (contact != null) {
            phoneNumberFromContactList = TextFieldValue(contact)

            if (
                _dialpadState.value.phoneNumber.text.isEmpty() ||
                _dialpadState.value.phoneNumber == phoneNumberFromContactList
            ) {
                _dialpadState.update {
                    it.copy(
                        phoneNumber = phoneNumberFromContactList
                    )
                }
            } else {
                _dialpadState.update {
                    it.copy(
                        showContactSelectionBox = true
                    )
                }
            }
        } else {
            // Failed to retrieve phone number
        }
    }

    // Release ToneGenerator resources when ViewModel is cleared
    override fun onCleared() {
        toneGenerator?.release()
        toneGenerator = null
        super.onCleared()
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as DialpadApplication)
                val contactsRepository = application.contactsRepository
                DialpadViewModel(contactsRepository = contactsRepository)
            }
        }
    }
}