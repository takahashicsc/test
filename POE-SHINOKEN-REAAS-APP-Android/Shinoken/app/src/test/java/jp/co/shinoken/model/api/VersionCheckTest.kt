package jp.co.shinoken.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets

class VersionCheckTest {
    private lateinit var versionCheck: VersionCheck

    @Before
    fun setUp() {
        val jsonString =
            IOUtils.toString(
                javaClass.getResourceAsStream("version_check.json"),
                StandardCharsets.UTF_8
            )
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        versionCheck = moshi.adapter(VersionCheck::class.java).fromJson(jsonString)!!
    }

    @Test
    fun tokenRefresh() {
        assertEquals(
            VersionCheck(
                success = true,
                code = "app_version_ok",
                message = "お使いのアプリは最新です。",
                version = "2.1.0"
            ),
            versionCheck
        )
    }
}