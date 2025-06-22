package com.example.dialpad.ui

import android.telephony.PhoneNumberUtils
import android.text.Selection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.Locale

class PhoneNumberVisualTransformation(
    countryCode: String = Locale.getDefault().country
) : VisualTransformation {

    private val phoneNumberFormatter =
        PhoneNumberUtil.getInstance().getAsYouTypeFormatter(countryCode)

    override fun filter(text: AnnotatedString): TransformedText {
        val transformation = reformat(text, Selection.getSelectionEnd(text))

        return TransformedText(
            AnnotatedString(transformation.formatted.orEmpty()),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return transformation.originalToTransformed[offset.coerceIn(transformation.originalToTransformed.indices)]
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return transformation.transformedToOriginal[offset.coerceIn(transformation.transformedToOriginal.indices)]
                }
            }
        )
    }

    private fun reformat(s: CharSequence, cursor: Int): Transformation {
        phoneNumberFormatter.clear()

        val curIndex = cursor - 1
        var formatted: String? = null
        var lastNonSeparator = 0.toChar()
        var hasCursor = false

        s.forEachIndexed { index, char ->
            if (PhoneNumberUtils.isNonSeparator(char)) {
                if (lastNonSeparator.code != 0) {
                    formatted = getFormattedNumber(lastNonSeparator, hasCursor)
                    hasCursor = false
                }
                lastNonSeparator = char
            }
            if (index == curIndex) {
                hasCursor = true
            }
        }

        if (lastNonSeparator.code != 0) {
            formatted = getFormattedNumber(lastNonSeparator, hasCursor)
        }
        val originalToTransformed = mutableListOf<Int>()
        val transformedToOriginal = mutableListOf<Int>()
        var specialCharsCount = 0
        formatted?.forEachIndexed { index, char ->
            if (!PhoneNumberUtils.isNonSeparator(char)) {
                specialCharsCount++
                transformedToOriginal.add(index - specialCharsCount)
            } else {
                originalToTransformed.add(index)
                transformedToOriginal.add(index - specialCharsCount)
            }
        }
        originalToTransformed.add(originalToTransformed.maxOrNull()?.plus(1) ?: 0)
        transformedToOriginal.add(transformedToOriginal.maxOrNull()?.plus(1) ?: 0)

        return Transformation(formatted, originalToTransformed, transformedToOriginal)
    }

    private fun getFormattedNumber(lastNonSeparator: Char, hasCursor: Boolean): String? {
        return if (hasCursor) {
            phoneNumberFormatter.inputDigitAndRememberPosition(lastNonSeparator)
        } else {
            phoneNumberFormatter.inputDigit(lastNonSeparator)
        }
    }

    private data class Transformation(
        val formatted: String?,
        val originalToTransformed: List<Int>,
        val transformedToOriginal: List<Int>
    )
}



class PhoneNumberTransformation(
    private val defaultCountry: String = Locale.getDefault().country
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.take(MAX_PHONE_LENGTH)
        val formatted = formatPhoneNumber(trimmed, defaultCountry)

        return TransformedText(
            text = AnnotatedString(formatted),
            offsetMapping = PhoneOffsetMapping(original = trimmed, formatted = formatted)
        )
    }

     fun formatPhoneNumber(number: String, country: String): String {
        val util = PhoneNumberUtil.getInstance()
        return try {
            val parsed = util.parse(number, country)
            util.format(parsed, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
        } catch (e: Exception) {
            number
        }
    }

    private inner class PhoneOffsetMapping(
        private val original: String,
        private val formatted: String
    ) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            val digits = original.take(offset).filter(Char::isDigit)
            return findPositionInFormatted(digits)
        }

        override fun transformedToOriginal(offset: Int): Int {
            val digits = formatted.take(offset).filter(Char::isDigit)
            return digits.length
        }

        private fun findPositionInFormatted(digits: String): Int {
            var digitCount = 0
            formatted.forEachIndexed { i, c ->
                if (c.isDigit()) {
                    if (digitCount == digits.length) {
                        return i
                    }
                    digitCount++
                }
            }
            return formatted.length
        }
    }

    companion object {
        private val MAX_PHONE_LENGTH = 15
    }
}
