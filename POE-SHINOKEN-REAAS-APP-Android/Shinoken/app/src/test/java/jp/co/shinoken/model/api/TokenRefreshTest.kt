package jp.co.shinoken.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets

class TokenRefreshTest {
    private lateinit var tokenRefresh: TokenRefresh

    @Before
    fun setUp() {
        val jsonString =
            IOUtils.toString(
                javaClass.getResourceAsStream("refresh.json"),
                StandardCharsets.UTF_8
            )
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        tokenRefresh = moshi.adapter(TokenRefresh::class.java).fromJson(jsonString)!!
    }

    @Test
    fun tokenRefresh() {
        val tokenRefresh = tokenRefresh.data

        assertEquals(
            Token(
                accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJjbGllbnQiOiJyZXNpZGVudGFwcCIsIm5hbWUiOiLlpKfkvLTlrrbmjIFlNmFmMjNmZSIsImVtYWlsIjoicnYtYmxhY2tob2xlKzQ0MGI0ZTJhZjc0MmUwMzNAcmVpdm8uY28uanAiLCJ0eXBlIjoiU3VicmVzaWRlbnQiLCJyb29tIjp7ImlkIjoyOCwibnVtYmVyIjoiMjAzIn0sImJ1aWxkaW5nIjp7ImlkIjoxMCwibmFtZSI6Iuemj-WyoeODhuOCueODiOW7uueJqeODnuODs-OCt-ODp-ODszg5OWY1M2UzIn0sImlhdCI6MTYxNjY3NTg4MSwiZXhwIjozMTk0MjU0Mjk5LCJjb2RlIjoiYTQ0MGI0ZTJhZjc0MmUwMzMiLCJzdWIiOiI0NDBiNGUyYWY3NDJlMDMzIiwiYXV0aF9pZCI6IlN1YnJlc2lkZW50XzE1In0.gTM-OGQikBmgAGZKfGh_Zx3PfUiMh8_EdbFXX60jvr0",
                refreshToken = null
            ), tokenRefresh
        )
    }
}