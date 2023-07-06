package jp.co.shinoken.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import jp.co.shinoken.extension.convertDateToString
import jp.co.shinoken.extension.convertStringToDate
import java.time.DateTimeException

@JsonClass(generateAdapter = true)
data class UserInfo(
    val data: User
)

@JsonClass(generateAdapter = true)
data class User(
    val sub: String,
    val name: String,

    @Json(name = "detected_accountables")
    val detectedAccountables: List<DetectedAccountable>
)

@JsonClass(generateAdapter = true)
data class DetectedAccountable(
    val id: Int,
    @Json(name = "auth_id")
    val authId: String,
    val code: String,
    val name: String,
    @Json(name = "residence_name")
    val residenceName: String,
    @Json(name = "contract_start_at")
    val contractStartAt: String,
    @Json(name = "contract_end_at")
    val contractEndAt: String,
) {
    fun getContractStartAtFormatString(): String {
        val startAtDate = contractStartAt.convertStringToDate() ?: throw DateTimeException("")
        return startAtDate.convertDateToString("yyyy.MM.dd")
    }

    fun getContractEndAtFormatString(): String {
        val startAtDate = contractEndAt.convertStringToDate() ?: throw DateTimeException("")
        return startAtDate.convertDateToString("yyyy.MM.dd")
    }
}