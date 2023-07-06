package jp.co.shinoken.model

import junit.framework.TestCase.assertEquals
import org.junit.Test

class BirthDayTest {
    private val birthDay = BirthDay(1991, 5, 9)

    @Test
    fun getBirthDayHyphenString() {
        assertEquals("1991-05-09", birthDay.birthDayHyphenString)
    }

    @Test
    fun getBirthDayFormatString() {
        assertEquals("1991年 5月 9日", birthDay.getBirthDayFormatString("%1\$d年 %2\$d月 %3\$d日"))
    }

    @Test
    fun parseBirthDay() {
        assertEquals(BirthDay(2020, 1, 1), BirthDay.parseBirthDay("2020-01-01"))
    }
}