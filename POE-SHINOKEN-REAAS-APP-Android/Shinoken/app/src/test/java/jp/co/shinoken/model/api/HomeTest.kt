package jp.co.shinoken.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets


class HomeTest {
    private lateinit var home: Home

    @Before
    fun setUp() {
        val jsonString =
            IOUtils.toString(
                javaClass.getResourceAsStream("home.json"),
                StandardCharsets.UTF_8
            )
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        home = moshi.adapter(HomeParent::class.java).fromJson(jsonString)!!.data
    }

    @Test
    fun getHome() {
        assertEquals(
            listOf(
                Menu(slug = Menu.Slug.Notice, title = "お知らせ /掲示板", subTitle = "新着24件", new = true),
                Menu(slug = Menu.Slug.Bill, title = "ご請求一覧", subTitle = "2020/12", new = true),
                Menu(slug = Menu.Slug.Procedure, title = "各種お手続き", subTitle = "進行中1件", new = true),
                Menu(slug = Menu.Slug.Inquiry, title = "お問い合わせ", subTitle = "進行中1件", new = true),
                Menu(slug = Menu.Slug.Faq, title = "よくある質問", subTitle = null, new = false),
                Menu(slug = Menu.Slug.Manual, title = "住まいのマニュアル", subTitle = null, new = false),
                Menu(slug = Menu.Slug.Benefit, title = "入居者様特典", subTitle = null, new = false),
                Menu(
                    slug = Menu.Slug.TrashCalendar,
                    title = "ゴミの日通知",
                    subTitle = null,
                    new = false
                ),
                Menu(slug = Menu.Slug.Media, title = "シノケンメディア", subTitle = null, new = false)
            ), home.menus
        )
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
                    daysToEndOfContract = 90,
                    residentName = "田中太郎",
                    residentNameKana = "タナカタロウ",
                    residentBirthDay = "2020-01-01",
                    residentTel = null,
                    residenceName = "テスト物件"
                ),
                checkForm = Me.CheckForm(
                    submittable = false,
                    submittedAt = null,
                    status = Me.CheckForm.Status.Draft
                ),
                insurance = null,
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
            home.me
        )

        assertEquals(
            WeatherParent(
                area = "福岡地方",
                weathers = listOf(
                    Weather(
                        time = "2021-02-08T17:00:00+09:00",
                        timeName = "今夜",
                        title = "晴れ",
                        code = "100",
                        groupCode = Weather.GroupCode.Sunny
                    ),
                    Weather(
                        time = "2021-02-09T00:00:00+09:00",
                        timeName = "明日",
                        title = "晴れ",
                        code = "100",
                        groupCode = Weather.GroupCode.Sunny
                    ),
                    Weather(
                        time = "2021-02-10T00:00:00+09:00",
                        timeName = "明後日",
                        title = "晴れ時々くもり",
                        code = "101",
                        groupCode = Weather.GroupCode.Sunny
                    )
                ),
                precipitations = listOf(
                    Precipitation(
                        time = "2021-02-08T18:00:00+09:00",
                        timeName = "１８時から００時まで",
                        value = "0"
                    ),
                    Precipitation(
                        time = "2021-02-09T00:00:00+09:00",
                        timeName = "００時から０６時まで",
                        value = "0"
                    ),
                    Precipitation(
                        time = "2021-02-09T06:00:00+09:00",
                        timeName = "０６時から１２時まで",
                        value = "0"
                    ),
                    Precipitation(
                        time = "2021-02-09T12:00:00+09:00",
                        timeName = "１２時から１８時まで",
                        value = "0"
                    ),
                    Precipitation(
                        time = "2021-02-09T18:00:00+09:00",
                        timeName = "１８時から２４時まで",
                        value = "0"
                    )
                ),
                temperatures = listOf(
                    Temperature(
                        time = "2021-02-09T00:00:00+09:00",
                        timeName = "明日朝",
                        title = "朝の最低気温",
                        value = "4",
                        slug = "min"
                    ),
                    Temperature(
                        time = "2021-02-09T09:00:00+09:00",
                        timeName = "明日日中",
                        title = "日中の最高気温",
                        value = "10",
                        slug = "max"
                    )
                )
            ),
            home.weather
        )
        assertEquals(
            listOf(
                Suggestion(
                    type = Suggestion.Type.Faq,
                    id = 36,
                    title = "共用部に蜂の巣があります。",
                    slug = "1476bbf143e4dfe9",
                    image = null
                ),
                Suggestion(
                    type = Suggestion.Type.Faq,
                    id = 37,
                    title = "廊下の共用灯が点灯していません。",
                    slug = "4652a6c1e7e43119",
                    image = null
                ),
                Suggestion(
                    type = Suggestion.Type.Faq,
                    id = 38,
                    title = "退去月の家賃はどうなりますか。",
                    slug = "902dd8307e6c1ebb",
                    image = null
                ),
                Suggestion(
                    type = Suggestion.Type.Faq,
                    id = 41,
                    title = "共用部に蜂の巣があります。",
                    slug = "6883cc9e61abf0e2",
                    image = null
                ),
                Suggestion(
                    type = Suggestion.Type.Faq,
                    id = 42,
                    title = "廊下の共用灯が点灯していません。",
                    slug = "a0b9db280a17bfc2",
                    image = null
                ),
                Suggestion(
                    type = Suggestion.Type.Faq,
                    id = 43,
                    title = "退去月の家賃はどうなりますか。",
                    slug = "f4cf59321273ce82",
                    image = null
                )
            ),
            home.suggestions
        )
    }
}
