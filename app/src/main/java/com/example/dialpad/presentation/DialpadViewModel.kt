package com.example.dialpad.presentation

import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dialpad.DialpadApplication
import com.example.dialpad.data.ContactResult
import com.example.dialpad.domain.ContactsRepository
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
        if (digit.length == 1)
            startTone(digit)
        val current = _dialpadState.value.phoneNumber

        try {
            val sb = StringBuilder(current.text)
            val insertPos = current.selection.start
            sb.replace(
                current.selection.start,
                current.selection.end,
                digit
            )
            val newCursorPos = insertPos + digit.length
            _dialpadState.update {
                it.copy(
                    phoneNumber = TextFieldValue(
                        text = sb.toString(),
                        selection = TextRange(newCursorPos, newCursorPos)  // Collapsed after insert
                    )
                )
            }
        } catch (e: IndexOutOfBoundsException) {
            _dialpadState.update {
                it.copy(
                    errorMessage = "Invalid Input"
                )
            }
        }
    }

    fun removeDigit() {
        val current = _dialpadState.value.phoneNumber
        if (current.text.isEmpty()) return  // Early exit if nothing to delete

        try {
            val sb = StringBuilder(current.text)
            val newCursorPos: Int
            if (current.selection.start == current.selection.end) {
                // Collapsed: delete char before cursor
                if (current.selection.start > 0) {
                    val deletePos = current.selection.start - 1
                    sb.delete(deletePos, deletePos + 1)
                    newCursorPos = deletePos
                } else {
                    return  // Nothing to delete
                }
            } else {
                // Range: delete selection
                sb.delete(current.selection.start, current.selection.end)
                newCursorPos = current.selection.start
            }
            _dialpadState.update {
                it.copy(
                    phoneNumber = TextFieldValue(
                        text = sb.toString(),
                        selection = TextRange(newCursorPos)
                    )
                )
            }
        } catch (e: IndexOutOfBoundsException) {
            _dialpadState.update {
                it.copy(
                    errorMessage = "Invalid Input"
                )
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


    fun getPhoneNumber(contactUri: Uri) {
        viewModelScope.launch {
            val contactResult = contactsRepository.getPhoneNumber(contactUri)
            when (contactResult) {
                is ContactResult.Success -> {
                    phoneNumberFromContactList = TextFieldValue(contactResult.phoneNumber)
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
                }

                is ContactResult.NoNumberFound -> {
                    _dialpadState.update {
                        it.copy(
                            errorMessage = "No contact found."
                        )
                    }
                }

                is ContactResult.Error -> {
                    _dialpadState.update {
                        it.copy(
                            errorMessage = contactResult.message
                        )
                    }
                }
            }
        }
    }


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