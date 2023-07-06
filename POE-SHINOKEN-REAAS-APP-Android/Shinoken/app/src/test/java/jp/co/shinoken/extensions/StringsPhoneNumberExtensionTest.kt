package jp.co.shinoken.extensions

import jp.co.shinoken.extension.toInternationalPhoneNumber
import jp.co.shinoken.extension.toPhoneNumber
import junit.framework.TestCase.assertEquals
import org.junit.Test

class StringsPhoneNumberExtensionTest {
    @Test
    fun toInternationalPhoneNumber() {
        val internationalPhoneNumber = "0904444444"
        assertEquals("+81904444444", internationalPhoneNumber.toInternationalPhoneNumber)
    }

    @Test
    fun toPhoneNumber() {
        val phoneNumber = "+81904444444"
        assertEquals("0904444444", phoneNumber.toPhoneNumber)
    }
}