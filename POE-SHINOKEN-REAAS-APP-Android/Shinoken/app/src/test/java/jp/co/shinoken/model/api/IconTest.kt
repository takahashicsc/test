package jp.co.shinoken.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets

class IconTest {
    private lateinit var icon: Icon

    @Before
    fun setUp() {
        val jsonString =
            IOUtils.toString(
                javaClass.getResourceAsStream("icon.json"),
                StandardCharsets.UTF_8
            )
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        icon = moshi.adapter(IconParent::class.java).fromJson(jsonString)!!.data
    }

    @Test
    fun getIcon() {
        assertEquals(
            Icon(
                key = "fccwkz3nhsdkuv2fptv4agd6thd9",
                contentType = null,
                publishUrl = "https://s3-ap-northeast-1.amazonaws.com/shinoken-demo.reivo.info/public/shared/user/icon/fccwkz3nhsdkuv2fptv4agd6thd9",
                variantUrl = VariantUrl(thumbnail = null, original = null)
            ),
            icon
        )
    }
}