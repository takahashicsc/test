package jp.co.shinoken.repository.impl

import jp.co.shinoken.R
import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.ShinokenApiService
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.*
import jp.co.shinoken.repository.HomeRepository
import java.util.*
import javax.inject.Inject

class DataHomeRepository @Inject constructor(
    private val shinokenApiService: ShinokenApiService
) : HomeRepository {
    private val mockHome: Home
        get() {
            return Home(
                suggestions = listOf(
                    Home.Suggestion(title = "ガスが使えない", iconRes = R.drawable.ic_help_icon1),
                    Home.Suggestion(title = "水漏れしている", iconRes = R.drawable.ic_help_icon2),
                    Home.Suggestion(title = "鍵を失くした", iconRes = R.drawable.ic_help_icon3),
                    Home.Suggestion(title = "鍵を掛け忘れた", iconRes = R.drawable.ic_help_icon4),
                    Home.Suggestion(title = "盗難被害に遭った", iconRes = R.drawable.ic_help_icon5),
                    Home.Suggestion(title = "騒音トラブル", iconRes = R.drawable.ic_help_icon6)
                ),
                homeMenus = HomeMenu.values().toList(),
                user = mockUser,
                weather = Home.Weather(
                    name = "",
                    iconRes = 0
                )
            )
        }

    private val mockUser: User
        get() {
            return User(
                name = "Test User",
                accounts = listOf(mockAccount, mockAccount2)
            )
        }

    private val mockAccount: Account
        get() {
            return Account(
                authId = "1111",
                name = "田中 太郎",
                namePhonetic = "タナカ タロウ",
                tel = "000-0000-0000",
                icon = null,
                accountType = AccountType.Contractor,
                userSub = null,
                startedAt = Date(),
                willEndAt = Date(),
                insurance = Account.Insurance(
                    status = "",
                    plan = "",
                    startedAt = Date(),
                    willEndAt = Date(),
                    contract = "契約中"
                ),
                building = Account.Building(
                    id = 0,
                    code = "",
                    name = "ハーモニーテラス 堺",
                    prefectureId = "",
                    prefectureName = "大阪府",
                    address = "",
                ),
                room = Account.Room(
                    id = 0,
                    code = "",
                    number = "203号室"
                ),
                contract = Account.Contract(
                    contractStartAt = Date(),
                    contractEndAt = Date(),
                    cancelledAt = null,
                    active = true,
                    roommatesCount = 3
                ),
                checkForm = Account.CheckForm(
                    submittable = false,
                    submittedAt = null,
                    status = "draft"
                )
            )
        }

    private val mockAccount2: Account
        get() {
            return Account(
                authId = "1111",
                name = "田中 太郎",
                namePhonetic = "タナカ タロウ",
                tel = "000-0000-0000",
                icon = null,
                accountType = AccountType.Contractor,
                userSub = null,
                startedAt = Date(),
                willEndAt = Date(),
                insurance = Account.Insurance(
                    status = "",
                    plan = "",
                    startedAt = Date(),
                    willEndAt = Date(),
                    contract = "契約中"
                ),
                building = Account.Building(
                    id = 0,
                    code = "",
                    name = "ハーモニーテラス 堺",
                    prefectureId = "",
                    prefectureName = "大阪府",
                    address = "",
                ),
                room = Account.Room(
                    id = 0,
                    code = "",
                    number = "204号室"
                ),
                contract = Account.Contract(
                    contractStartAt = Date(),
                    contractEndAt = Date(),
                    cancelledAt = null,
                    active = true,
                    roommatesCount = 3
                ),
                checkForm = Account.CheckForm(
                    submittable = false,
                    submittedAt = null,
                    status = "draft"
                )
            )
        }

    override fun getHomeData(): ApiResponse<Home> {
        return ApiResponse(
            status = Status.SUCCESS,
            data = mockHome,
            appError = null
        )
    }
}