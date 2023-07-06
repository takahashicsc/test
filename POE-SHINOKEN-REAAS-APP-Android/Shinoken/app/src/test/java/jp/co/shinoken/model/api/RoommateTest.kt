package jp.co.shinoken.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets

class RoommateTest {
    private lateinit var roommates: Roommates

    @Before
    fun setUp() {
        val jsonString =
            IOUtils.toString(
                javaClass.getResourceAsStream("roommate_requests.json"),
                StandardCharsets.UTF_8
            )
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        roommates = moshi.adapter(Roommates::class.java).fromJson(jsonString)!!
    }

    @Test
    fun getRoommate() {
        assertEquals(1, roommates.total)
        val roommate = roommates.data

        assertEquals(
            listOf(
                Roommate(
                    id = 7,
                    name = "同居人A",
                    birthday = "2000-10-10",
                    email = "test+50911d2b@example.com",
                    tel = null,
                    status = Roommate.Status.Approved
                )
            ),
            roommate
        )
    }
}