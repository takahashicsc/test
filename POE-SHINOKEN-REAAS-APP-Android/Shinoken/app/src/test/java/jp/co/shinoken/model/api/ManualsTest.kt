package jp.co.shinoken.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets

class ManualsTest {
    private lateinit var manuals: Manuals

    @Before
    fun setUp() {
        val jsonString =
            IOUtils.toString(
                javaClass.getResourceAsStream("manuals.json"),
                StandardCharsets.UTF_8
            )
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        manuals = moshi.adapter(Manuals::class.java).fromJson(jsonString)!!
    }

    @Test
    fun getInquiries() {
        assertEquals(4, manuals.total)
        val manual = manuals.data.first()
        assertEquals(
            Manual(
                id = 30,
                title = "マニュアルman5dd0c25a",
                asset = Asset(
                    key = "assetsman5dd0c25a",
                    contentType = "application/pdf",
                    url = "https://s3-ap-northeast-1.amazonaws.com/shinoken-demo.reivo.info/public/shared/assetsman5dd0c25a"
                ),
            ),
            manual
        )
    }
}