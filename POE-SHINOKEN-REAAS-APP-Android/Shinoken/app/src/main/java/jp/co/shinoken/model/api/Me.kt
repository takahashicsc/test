package jp.co.shinoken.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import jp.co.shinoken.extension.convertDateToString
import jp.co.shinoken.extension.convertStringToDate
import jp.co.shinoken.extension.differenceDays
import java.util.*

/*
{
  "data": {
    "auth_id": "Resident_15",
    "accountable_type": "Resident",
    "user_sub": "041fac994a3ddd7a",
    "name": "坂上田村麻呂e6af23fe",
    "name_phonetic": "サカノウエノタムラマロe6af23fe",
    "email": "rv-blackhole+041fac994a3ddd7a@reivo.co.jp",
    "tel": "000-0000-0000+041fac994a3ddd7a",
    "building": {
      "id": 10,
      "code": "899f53e3",
      "name": "福岡テスト建物マンション899f53e3",
      "prefecture_id": 40,
      "prefecture_name": "福岡県",
      "address": "812-8577"
    },
    "room": {
      "id": 28,
      "code": "899f53e3_203",
      "number": "203"
    },
    "icon": {
      "key": "l5clddndifiy41ualrsjgz9wl8mw",
      "content_type": "image/png",
      "publish_url": "https://s3-ap-northeast-1.amazonaws.com/shinoken-demo.reivo.info/public/shared/user/icon/l5clddndifiy41ualrsjgz9wl8mw",
      "variant_urls": {}
    },
    "contract": {
      "contract_start_at": "2016-11-01T00:00:00.000Z",
      "contract_end_at": "2090-10-20T00:00:00.000Z",
      "cancelled_at": null,
      "active": true,
      "roommates_count": 0
    },
    "check_form": {
      "submittable": false,
      "submitted_at": null,
      "status": "draft"
    },
    "lifelines": [
      {
        "name": "福岡水道局899f53e3支店",
        "tel": "0120-000-000"
      },
      {
        "name": "福岡ガスセンター899f53e3支店",
        "tel": "0120-000-000"
      }
    ],
    "detected_accountables": [
      {
        "id": 15,
        "auth_id": "Resident_15",
        "code": "e6af23fe",
        "name": "坂上田村麻呂e6af23fe",
        "residence_name": "福岡テスト建物マンション899f53e3 203",
        "contract_start_at": "2016-11-01T00:00:00.000Z",
        "contract_end_at": "2090-10-20T00:00:00.000Z"
      }
    ],
    "meta": {
      "terms_of_service": "https://shinoken.reivo.info/pages/terms-of-service",
      "privacy_policy": "https://shinoken.reivo.info/pages/privacy-policy",
      "mail_transfer": "https://www.post.japanpost.jp/service/tenkyo/index.html"
    }
  }
}
 */
@JsonClass(generateAdapter = true)
data class MeParent(
    val data: Me
)

@JsonClass(generateAdapter = true)
data class Me(
    @Json(name = "auth_id")
    val authId: String,
    @Json(name = "accountable_type")
    val accountableType: AccountableType,
    @Json(name = "user_sub")
    val userSub: String,
    val name: String,
    @Json(name = "name_phonetic")
    val nameKana: String?,
    @Json(name = "email")
    val email: String,
    val tel: String?,
    val building: Building,
    val room: Room,
    val icon: Icon?,
    val contract: Contract,
    @Json(name = "resident_insurance")
    val insurance: Insurance?,
    @Json(name = "check_form")
    val checkForm: CheckForm,
    val lifelines: List<Lifeline>,
    @Json(name = "detected_accountables")
    val detectedAccountables: List<DetectedAccountable>,
    val meta: Meta,
    @Json(name = "members_count")
    val membersCount: Int //使用廃止

) {
    enum class AccountableType {
        Resident, // 契約者
        Subresident, // 入居者
        Roommate // 同居人
    }

    // チェックフォーム
    @JsonClass(generateAdapter = true)
    data class CheckForm(
        val submittable: Boolean,
        @Json(name = "submitted_at")
        val submittedAt: String?,
        val status: Status
    ) {
        enum class Status {
            @Json(name = "draft")
            Draft,

            @Json(name = "submitted")
            SUBMITTED,
        }
    }

    // 保険
    @JsonClass(generateAdapter = true)
    data class Insurance(
        val company: String,
        val plan: String?,
        @Json(name = "start_at")
        val startAt: String,
        @Json(name = "end_at")
        val endAt: String,
        val amount: Int,
        val active: Boolean,
    ) {
        fun getStartedAtFormatString(): String {
            val lastUpdatedAtDate = startAt.convertStringToDate()
            return lastUpdatedAtDate?.convertDateToString("yyyy.MM.dd") ?: ""
        }

        fun getEndAtFormatString(): String {
            val lastUpdatedAtDate = endAt.convertStringToDate()
            return lastUpdatedAtDate?.convertDateToString("yyyy.MM.dd") ?: ""
        }
    }

    @JsonClass(generateAdapter = true)
    data class Building(
        val id: Int,
        val code: String,
        val name: String,
        @Json(name = "prefecture_id")
        val prefectureId: String?,
        @Json(name = "prefecture_name")
        val prefectureName: String?,
        val address: String
    )

    // 部屋
    @JsonClass(generateAdapter = true)
    data class Room(
        val id: Int,
        val code: String,
        val number: String
    )

    // 契約
    @JsonClass(generateAdapter = true)
    data class Contract(
        @Json(name = "contract_start_at")
        val contractStartAt: String,
        @Json(name = "contract_end_at")
        val contractEndAt: String,
        @Json(name = "cancelled_at")
        val cancelledAt: String?,
        val active: Boolean,
        @Json(name = "roommates_count")
        val roommatesCount: Int,
        @Json(name = "days_to_end_of_contract")
        val daysToEndOfContract: Int,
        @Json(name = "resident_name")
        val residentName: String?,
        @Json(name = "resident_name_phonetic")
        val residentNameKana: String?,
        @Json(name = "resident_birthday")
        val residentBirthDay: String?,
        @Json(name = "resident_tel")
        val residentTel: String?,
        @Json(name = "residence_name")
        val residenceName: String?
    ) {
        fun getStartedAtFormatString(): String {
            val lastUpdatedAtDate = contractStartAt.convertStringToDate()
            return lastUpdatedAtDate?.convertDateToString("yyyy.MM.dd") ?: ""
        }

        fun getEndAtFormatString(): String {
            val lastUpdatedAtDate = contractEndAt.convertStringToDate()
            return lastUpdatedAtDate?.convertDateToString("yyyy.MM.dd") ?: ""
        }
    }

    // ライフライン
    @JsonClass(generateAdapter = true)
    data class Lifeline(
        val name: String,
        val tel: String,
        val kind: String
    )

    // 現在ログイン中のアカウントが切り替え可能な別のアカウントの一覧
    @JsonClass(generateAdapter = true)
    data class DetectedAccountable(
        val id: Int,
        @Json(name = "auth_id")
        val authId: String,
        val code: String,
        val name: String,
        @Json(name = "residence_name")
        val residenceName: String
    )

    @JsonClass(generateAdapter = true)
    data class MetaParent(
        val data: Meta
    )

    @JsonClass(generateAdapter = true)
    data class Meta(
        @Json(name = "terms_of_service")
        val termOfService: String,
        @Json(name = "privacy_policy")
        val privacyPolicy: String,
        @Json(name = "mail_transfer")
        val mailTransfer: String,
        @Json(name = "cancel_request")
        val cancelRequest: String,
    )
}

