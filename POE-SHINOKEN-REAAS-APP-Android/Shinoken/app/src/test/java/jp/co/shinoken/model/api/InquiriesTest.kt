package jp.co.shinoken.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets

class InquiriesTest {
    private lateinit var inquiries: Inquiries

    @Before
    fun setUp() {
        val jsonString =
            IOUtils.toString(
                javaClass.getResourceAsStream("inquiries.json"),
                StandardCharsets.UTF_8
            )
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        inquiries = moshi.adapter(Inquiries::class.java).fromJson(jsonString)!!
    }

    @Test
    fun getInquiries() {
        assertEquals(2, inquiries.total)
        val inquiry = inquiries.data.first()
        assertEquals(
            Inquiry(
                id = 2,
                kind = "shared_space",
                email = "rv-blackhole+041fac994a3ddd7a@reivo.co.jp",
                body = "I have a dream.",
                ip = "117.102.197.57"
            ),
            inquiry
        )
    }
}