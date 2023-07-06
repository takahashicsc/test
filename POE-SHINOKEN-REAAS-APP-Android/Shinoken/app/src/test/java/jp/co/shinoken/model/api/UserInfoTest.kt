package jp.co.shinoken.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets


class UserInfoTest {
    private lateinit var userInfo: UserInfo

    @Before
    fun setUp() {
        val jsonString =
            IOUtils.toString(
                javaClass.getResourceAsStream("user_info.json"),
                StandardCharsets.UTF_8
            )
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        userInfo = moshi.adapter(UserInfo::class.java).fromJson(jsonString)!!
    }

    @Test
    fun getUserInfo() {
        assertEquals("c836dcc4-5d12-4f98-9824-8edd5d674244", userInfo.data.sub)
        assertEquals("takuya.ohashi@actbe.co.jp", userInfo.data.name)

        val detectedAccountable = userInfo.data.detectedAccountables.first()
        assertEquals(20, detectedAccountable.id)
        assertEquals("takuya.ohashiC", detectedAccountable.name)
        assertEquals("testa1105", detectedAccountable.code)
        assertEquals("Resident_20", detectedAccountable.authId)
        assertEquals("テスト建物1 1105", detectedAccountable.residenceName)
        assertEquals("2019-12-01T00:00:00.000Z", detectedAccountable.contractStartAt)
        assertEquals("2021-11-30T00:00:00.000Z", detectedAccountable.contractEndAt)
        assertEquals("2019.12.01", detectedAccountable.getContractStartAtFormatString())
        assertEquals("2021.11.30", detectedAccountable.getContractEndAtFormatString())
    }
}