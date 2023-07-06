package jp.co.shinoken.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets

class ResideParentTest {
    private lateinit var reside: Reside

    @Before
    fun setUp() {
        val jsonString =
            IOUtils.toString(
                javaClass.getResourceAsStream("resider.json"),
                StandardCharsets.UTF_8
            )
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        reside = moshi.adapter(ResideParent::class.java).fromJson(jsonString)!!.data
    }

    @Test
    fun getResider() {
        assertEquals(
            Reside(
                resident = Resident(
                    id = 15,
                    authId = "Resident_15",
                    code = "e6af23fe",
                    name = "坂上田村麻呂e6af23fe",
                    residenceName = "福岡テスト建物マンション899f53e3 203",
                    nameKana = "サカノウエノタムラマロe6af23fe",
                    birthday = "1970-12-21",
                    contacts = listOf(
                        Contact(kind = Contact.Kind.Tel, value = "000-0000-0000+041fac994a3ddd7a"),
                        Contact(kind = Contact.Kind.Email, value = "rv-blackhole+041fac994a3ddd7a@reivo.co.jp")
                    ),
                    myself = true
                ),
                subresident = Resident(
                    id = 15,
                    authId = "Subresident_15",
                    code = "e6af23fe",
                    name = "大伴家持e6af23fe",
                    residenceName = "福岡テスト建物マンション899f53e3 203",
                    nameKana = "オオトモノヤカモチe6af23fe",
                    birthday = "1961-03-27",
                    contacts = listOf(
                        Contact(kind = Contact.Kind.Tel, value = "000-0000-0000+440b4e2af742e033"),
                        Contact(kind = Contact.Kind.Email, value = "rv-blackhole+440b4e2af742e033@reivo.co.jp")
                    ),
                    myself = false
                ),
                roommates = listOf(),
                roommateRequests = listOf()
            ), reside
        )
    }
}