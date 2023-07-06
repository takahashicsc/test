package jp.co.shinoken.model

import jp.co.shinoken.R
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalTime

class HomeMessageTest {
    @Test
    fun getHomeMessageRes() {
        assertEquals(
            HomeMessage(LocalTime.of(12,0,0)).homeMessageRes,
            R.string.home_message_daytime
        )

        assertEquals(
            HomeMessage(LocalTime.of(16,0,0)).homeMessageRes,
            R.string.home_message_night
        )

        assertEquals(
            HomeMessage(LocalTime.of(5,0,0)).homeMessageRes,
            R.string.home_message_morning
        )
    }
}