package jp.co.shinoken.ui.fragment.faq

import androidx.lifecycle.SavedStateHandle
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import jp.co.shinoken.CoroutinesTestRule
import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.model.*
import jp.co.shinoken.model.api.*
import jp.co.shinoken.model.api.Home
import jp.co.shinoken.repository.FaqRepository
import jp.co.shinoken.repository.HomeRepository
import junit.framework.TestCase.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

class FaqsViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    private val mockFaqs = Faqs(
        categories = listOf(
            FaqCategory(
                id = 1,
                name = "共用部",
                path = "",
                image = null,
                status = Status.Opened,
                serialCode = 0
            ),
            FaqCategory(
                id = 2,
                name = "室内",
                path = "",
                image = null,
                status = Status.Opened,
                serialCode = 1
            ),
            FaqCategory(
                id = 3,
                name = "契約関連",
                path = "",
                image = null,
                status = Status.Opened,
                serialCode = 2
            ),
            FaqCategory(
                id = 4,
                name = "家賃",
                path = "",
                image = null,
                status = Status.Opened,
                serialCode = 3
            ),
            FaqCategory(
                id = 5,
                name = "寒冷地",
                path = "",
                image = null,
                status = Status.Opened,
                serialCode = 4
            ),
            FaqCategory(
                id = 6,
                name = "各種変更",
                path = "",
                image = null,
                status = Status.Opened,
                serialCode = 5
            ),
            FaqCategory(
                id = 7,
                name = "各種変更",
                path = "",
                image = null,
                status = Status.Opened,
                serialCode = 6
            ),
            FaqCategory(
                id = 8,
                name = "生活ルール",
                path = "",
                image = null,
                status = Status.Opened,
                serialCode = 6
            ),
            FaqCategory(
                id = 9,
                name = "その他",
                path = "",
                image = null,
                status = Status.Opened,
                serialCode = 6
            )
        ),
        data = listOf(
            FaqContent(
                id = 1,
                title = "共用部に蜂の巣があります。",
                slug = "1476bbf143e4dfe9",
                contentText = "ガスメーターの安全装置がはたらき、ガスの供給が...",
                images = listOf(),
                links = listOf(),
                categories =
                listOf(
                    FaqCategory(
                        id = 1,
                        name = "共用部",
                        path = "",
                        image = null,
                        status = Status.Opened,
                        serialCode = 0
                    )
                )
            ),
            FaqContent(
                id = 2,
                title = "廊下の共用灯が点灯していません。",
                slug = "1476bbf143e4dfe9",
                contentText = "ガスメーターの安全装置がはたらき、ガスの供給が...",
                images = listOf(),
                links = listOf(),
                categories =
                listOf(
                    FaqCategory(
                        id = 2,
                        name = "室内",
                        path = "",
                        image = null,
                        status = Status.Opened,
                        serialCode = 1
                    )
                )
            ),
            FaqContent(
                id = 3,
                title = "退去月の家賃はどうなりますか。",
                slug = "1476bbf143e4dfe9",
                contentText = "ガスメーターの安全装置がはたらき、ガスの供給が...",
                images = listOf(),
                links = listOf(),
                categories =
                listOf(
                    FaqCategory(
                        id = 2,
                        name = "室内",
                        path = "",
                        image = null,
                        status = Status.Opened,
                        serialCode = 1
                    ),
                    FaqCategory(
                        id = 3,
                        name = "契約関連",
                        path = "",
                        image = null,
                        status = Status.Opened,
                        serialCode = 2
                    )
                )
            ),
        ),
        total = 10,
    )

    private val mockHome = Home(
        menus = listOf(
            Menu(slug = Menu.Slug.Notice, title = "お知らせ /掲示板", subTitle = "新着24件", new = true),
            Menu(slug = Menu.Slug.Bill, title = "ご請求一覧", subTitle = "2020/12", new = true),
            Menu(slug = Menu.Slug.Procedure, title = "各種お手続き", subTitle = "進行中1件", new = true),
            Menu(slug = Menu.Slug.Inquiry, title = "お問い合わせ", subTitle = "進行中1件", new = true),
            Menu(slug = Menu.Slug.Faq, title = "よくある質問", subTitle = null, new = false),
            Menu(slug = Menu.Slug.Manual, title = "住まいのマニュアル", subTitle = null, new = false),
            Menu(slug = Menu.Slug.Benefit, title = "入居者特典", subTitle = null, new = false),
            Menu(
                slug = Menu.Slug.TrashCalendar,
                title = "ゴミの日通知",
                subTitle = null,
                new = false
            ),
            Menu(slug = Menu.Slug.Media, title = "シノケンメディア", subTitle = null, new = false)
        ),
        me = Me(
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
        weather = WeatherParent(
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
        suggestions = listOf(
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
        )
    )

    private val mockSuccessRepository = mockk<FaqRepository> {
        // パターンの設定
        coEvery { getFaqs() } returns ApiResponse(
            status = jp.co.shinoken.api.Status.SUCCESS,
            data = mockFaqs,
            appError = null
        )
    }

    private val mockHomeRepository = mockk<HomeRepository> {
        // パターンの設定
        coEvery { getHome() } returns ApiResponse(
            status = jp.co.shinoken.api.Status.SUCCESS,
            data = mockHome,
            appError = null
        )
    }

    private val mockErrorRepository = mockk<FaqRepository> {
        // パターンの設定
        coEvery { getFaqs() } returns ApiResponse(
            status = jp.co.shinoken.api.Status.ERROR,
            data = null,
            appError = AppError.ApiException.ApiOtherErrors(Throwable())
        )
    }


    private val savedStateHandle = SavedStateHandle()

    @Test
    fun fetchSuccess() = runBlockingTest {
        val viewModel = FaqsViewModel(mockSuccessRepository, mockHomeRepository, savedStateHandle)
        viewModel.fetch()
        coVerify { mockSuccessRepository.getFaqs() }
        assertEquals(9, viewModel.faqs.value?.categories?.size)
        assertEquals(3, viewModel.faqs.value?.data?.size)
        assertEquals(10, viewModel.faqs.value?.total)
    }

    @Test
    fun fetchResultError() = runBlockingTest {
        val viewModel = FaqsViewModel(mockErrorRepository, mockHomeRepository, savedStateHandle)
        viewModel.fetch()
        coVerify { mockErrorRepository.getFaqs() }
        assertFalse(viewModel.isLoading.value)
        assertTrue(viewModel.appError.value is AppError.ApiException.ApiOtherErrors)
    }

    @Test
    fun setSelectCategory() = runBlockingTest {
        val viewModel = FaqsViewModel(mockSuccessRepository, mockHomeRepository, savedStateHandle)
        viewModel.fetch()
        coVerify { mockSuccessRepository.getFaqs() }

        val selectCategoryDataBefore = viewModel.faqs.value?.categories?.first()!!
        val selectCategoryBefore = FaqCategory(
            selectCategoryDataBefore.id,
            selectCategoryDataBefore.name,
            selectCategoryDataBefore.path,
            selectCategoryDataBefore.image,
            selectCategoryDataBefore.status,
            selectCategoryDataBefore.serialCode,
            false
        )
        val faqContents: List<FaqContent> =
            mockFaqs.data.filter { it.categories.contains(selectCategoryBefore) }
        assertEquals(
            faqContents,
            viewModel.selectFaqContents.value
        )

        viewModel.setSelectCategory(mockFaqs.categories[1])

        val selectCategoryDataAfter = viewModel.faqs.value?.categories!![1]
        val selectCategoryAfter = FaqCategory(
            selectCategoryDataAfter.id,
            selectCategoryDataAfter.name,
            selectCategoryDataAfter.path,
            selectCategoryDataAfter.image,
            selectCategoryDataAfter.status,
            selectCategoryDataAfter.serialCode,
            false
        )
        val faqContentsAfter: List<FaqContent> =
            mockFaqs.data.filter { it.categories.contains(selectCategoryAfter) }
        assertEquals(
            faqContentsAfter,
            viewModel.selectFaqContents.value
        )
    }
}