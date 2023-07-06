package jp.co.shinoken.model

import com.squareup.moshi.Json
import java.util.*

// 物件
data class Account(
    @Json(name = "auth_id")
    val authId: String,
    val name: String,
    @Json(name = "name_phonetic")
    val namePhonetic: String,
    val tel: String,
    val icon: Icon?,
    @Json(name = "accountable_type")
    val accountType: AccountType,
    @Json(name = "user_sub")
    val userSub: String?,
    val startedAt: Date,
    val willEndAt: Date,
    val insurance: Insurance,
    val building: Building,
    val room: Room,
    val remainingDays: Int = 30, /* FIXME: デフォルト値外すと思う*/
    val renewFee: Int = 10000, /* FIXME: デフォルト値外すと思う*/
    val serviceFee: Int = 3000, /* FIXME: デフォルト値外すと思う*/
    val contract: Contract,
    val numberOfUsers: Int = 3,
    val checkForm: CheckForm
) {

    // 保険
    data class Insurance(
        val status: String,
        val plan: String,
        val startedAt: Date,
        val willEndAt: Date,
        val contract: String /* 契約 */
    )

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
    data class Room(
        val id: Int,
        val code: String,
        val number: String
    )

    data class Icon(
        val id: String?,
        val contentType: String?,
        @Json(name = "publish_url")
        val publishUrl: String,
        @Json(name = "variant_urls")
        val variantUrls: List<String>?
    )

    // 契約
    data class Contract(
        @Json(name = "contract_start_at")
        val contractStartAt: Date,
        @Json(name = "contract_end_at")
        val contractEndAt: Date,
        @Json(name = "cancelled_at")
        val cancelledAt: Date?,
        val active: Boolean,
        @Json(name = "roommates_count")
        val roommatesCount: Int
    )

    // チェックフォーム
    data class CheckForm(
        val submittable: Boolean,
        @Json(name = "submitted_at")
        val submittedAt: Date?,
        val status: String
    ) {
        enum class Status {
            @Json(name = "draft")
            Draft
        }
    }
}