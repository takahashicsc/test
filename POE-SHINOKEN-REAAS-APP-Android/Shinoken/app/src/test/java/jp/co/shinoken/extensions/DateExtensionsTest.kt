package jp.co.shinoken.extensions

import jp.co.shinoken.extension.convertStringToDate
import jp.co.shinoken.extension.differenceDays
import junit.framework.TestCase.assertEquals
import org.junit.Test

class DateExtensionsTest {
    @Test
    fun differenceDays() {
        val currentDate = "2021-04-15T00:00:00.000Z".convertStringToDate()!!
        assertEquals(15, "2021-04-30T00:00:00.000Z".convertStringToDate()!!.differenceDays(currentDate))
    }
}