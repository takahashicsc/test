package jp.co.shinoken.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets

class MeTest {
    private lateinit var me: Me

    @Before
    fun setUp() {
        val jsonString =
            IOUtils.toString(
                javaClass.getResourceAsStream("me.json"),
                StandardCharsets.UTF_8
            )
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        me = moshi.adapter(MeParent::class.java).fromJson(jsonString)!!.data
    }

    @Test
    fun getMe() {
        val me = me
        assertEquals(
            Me(
                authId = "Resident_15",
                accountableType = Me.AccountableType.Resident,
                userSub = "041fac994a3ddd7a",
                name = "坂上田村麻呂e6af23fe",
                nameKana = "サカノウエノタムラマロe6af23fe",
                email = "rv-blackhole+041fac994a3ddd7a@reivo.co.jp",
                tel = "000-0000-0000+041fac994a3ddd7a",
                building = Me.Building(
                    id = 6,
                    code = "899f53e3",
                    name = "福岡テスト建物マンション899f53e3",
                    prefectureId = "40",
                    prefectureName = "福岡県",
                    address = "812-8577"
                ),
                room = Me.Room(id = 18, code = "899f53e3_203", number = "203"),
                icon = Icon(
                    key = "fccwkz3nhsdkuv2fptv4agd6thd9",
                    contentType = null,
                    publishUrl = "https://s3-ap-northeast-1.amazonaws.com/shinoken-demo.reivo.info/public/shared/user/icon/fccwkz3nhsdkuv2fptv4agd6thd9",
                    variantUrl = VariantUrl(thumbnail = null, original = null)
                ),
                contract = Me.Contract(
                    contractStartAt = "2016-11-01T00:00:00.000Z",
                    contractEndAt = "2090-10-20T00:00:00.000Z",
                    cancelledAt = null,
                    active = true,
                    roommatesCount = 0,
                    daysToEndOfContract = 2000,
                    residentName = "田中太郎",
                    residentNameKana = "タナカタロウ",
                    residentBirthDay = "2020-01-01",
                    residentTel = null,
                    residenceName = "テスト物件"
                ),
                insurance = null,
                checkForm = Me.CheckForm(
                    submittable = false,
                    submittedAt = null,
                    status = Me.CheckForm.Status.Draft
                ),
                lifelines = listOf(
                    Me.Lifeline(
                        name = "福岡水道局899f53e3支店",
                        tel = "0120-000-000",
                        kind = "水道"
                    ), Me.Lifeline(name = "福岡ガスセンター899f53e3支店", tel = "0120-000-000", kind = "ガス")
                ),
                detectedAccountables = listOf(
                    Me.DetectedAccountable(
                        id = 15,
                        authId = "Resident_15",
                        code = "e6af23fe",
                        name = "坂上田村麻呂e6af23fe",
                        residenceName = "福岡テスト建物マンション899f53e3 203"
                    )
                ),
                meta = Me.Meta(
                    termOfService = "https://shinoken.reivo.info/pages/terms-of-service",
                    privacyPolicy = "https://shinoken.reivo.info/pages/privacy-policy",
                    mailTransfer = "https://www.post.japanpost.jp/service/tenkyo/index.html",
                    cancelRequest = ""
                ),
                membersCount = 1,
            ),
            me
        )
    }
}