package jp.co.shinoken.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ResideParent(
    val data: Reside
)

@JsonClass(generateAdapter = true)
data class Reside(
    val resident: Resident,
    val subresident: Resident?,
    val roommates: List<Resident>,
    @Json(name = "roommate_requests")
    val roommateRequests: List<RoommateRequest>
)

@JsonClass(generateAdapter = true)
data class Resident(
    val id: Int,
    @Json(name = "auth_id")
    val authId: String,
    val code: String,
    val name: String,
    @Json(name = "residence_name")
    val residenceName: String?,
    @Json(name = "name_phonetic")
    val nameKana: String?,
    val birthday: String?,
    val contacts: List<Contact>,
    val myself: Boolean
)

@JsonClass(generateAdapter = true)
data class RoommateRequest(
    val id: Int,
    val code: String,
    val name: String?,
    val birthday: String?,
    val email: String?,
    val tel: String?,
    val status: Status,
) {
    enum class Status {
        @Json(name = "pending")
        Pending,

        @Json(name = "approved")
        Approved,

        @Json(name = "rejected")
        Rejected
    }
}

@JsonClass(generateAdapter = true)
data class Contact(
    val kind: Kind,
    val value: String,
) {
    enum class Kind {
        @Json(name = "email")
        Email,

        @Json(name = "tel")
        Tel
    }
}