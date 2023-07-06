package jp.co.shinoken.model.api

import org.apache.commons.io.IOUtils
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets

class BenefitsTest {
    private lateinit var benefits: Benefits

    @Before
    fun setUp() {
        val jsonString =
            IOUtils.toString(
                javaClass.getResourceAsStream("benefits.json"),
                StandardCharsets.UTF_8
            )
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        benefits = moshi.adapter(Benefits::class.java).fromJson(jsonString)!!
    }

    @Test
    fun getBenefits() {
        assertEquals(1, benefits.total)

        val benefit = benefits.data.first()
        assertEquals(39, benefit.id)
        assertEquals("test", benefit.title)
        assertEquals("a79a4ee180e4b0e4", benefit.slug)
        assertEquals("2021-02-17T21:26:21.000Z", benefit.startAt)
        assertEquals("https://google.co.jp", benefit.url)
    }
}