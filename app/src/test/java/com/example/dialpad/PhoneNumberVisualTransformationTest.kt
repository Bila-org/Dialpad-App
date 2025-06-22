package com.example.dialpad

import androidx.compose.ui.text.AnnotatedString
import com.example.dialpad.ui.PhoneNumberTransformation
import com.example.dialpad.ui.PhoneNumberVisualTransformation
import com.google.i18n.phonenumbers.AsYouTypeFormatter
import com.google.i18n.phonenumbers.PhoneNumberUtil
import junit.framework.TestCase.assertEquals
import org.junit.Test


internal class PhoneNumberVisualTransformationTest {

    // constants
    private val usCountry = "US"
    private val ukCountry = "UK"
    private val maxLength = 15


    private lateinit var phoneNumberUtil: PhoneNumberUtil
    private lateinit var formatter: AsYouTypeFormatter
    private lateinit var transformation: PhoneNumberVisualTransformation

    /*
    @BeforeEach
    fun setUp() {
        // Mock PhoneNumberUtil and AsYouTypeFormatter
        phoneNumberUtil = mock()
        formatter = mock()
        whenever(phoneNumberUtil.getAsYouTypeFormatter("US")).thenReturn(formatter)
        transformation = PhoneNumberVisualTransformation("US")
    }

    @Test
    fun testPartialUSnumber(){
        val input = AnnotatedString("234")
        val transformation = PhoneNumberVisualTransformation(usCountry)
        val transformed = transformation.filter(input)
        val expected = "+1 (234) "
        assertEquals(expected, transformed)
    }

*/
    //private val transformation  = PhoneNumberVisualTransformation("US")

  /*  @Test
    fun testPhoneNumberFormatting() {
        val input = AnnotatedString("1234567890")
        val transformed = transformation.filter(input)
        val expected  = "(123) 456-7890"
        assertEquals(expected, transformed.text.text)
    }

    @Test
    fun testPartialInput() {
        val input = AnnotatedString("123")
        val transformed = transformation.filter(input)
        val expected = "(123) "
        assertEquals(expected, transformed.text.text)
    }

    @Test
    fun testCursorPosition() {
        val input = AnnotatedString("123")
        val transformed = transformation.filter(input)
        // Test cursor position after "123" (should map to after "(123) ")
        val cursorOriginal = 3
        val cursorTransformed = transformed.offsetMapping.originalToTransformed(cursorOriginal)
        assertEquals(6, cursorTransformed) // After "(123) "
    }

    @Test
    fun testInvalidInput() {
        val input = AnnotatedString("123abc456")
        val transformed = transformation.filter(input)
        val expected = "(123) 456"
        assertEquals(expected, transformed.text.text)
    }
*/
}