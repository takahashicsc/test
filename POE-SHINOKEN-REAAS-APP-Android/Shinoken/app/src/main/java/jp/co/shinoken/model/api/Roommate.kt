package jp.co.shinoken.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Roommates(
    val total: Int,
    val data: List<Roommate>
)
@JsonClass(generateAdapter = true)
data class ResponseSuccessRoommate(
    val message: String,
    val data: Roommate
)
@JsonClass(generateAdapter = true)
data class RoommateDetail(
    val data: Roommate
)

/*
{
    "id": 7,
    "name": "同居人A",
    "birthday": "2000-10-10",
    "email": "test+50911d2b@example.com",
    "tel": null,
    "status": "pending",
    "approved_at": null,
    "created_at": "2021-02-17T19:31:07.997Z"
}
 */
@JsonClass(generateAdapter = true)
data class Roommate(
    val id: Int,
    val name: String,
    val birthday: String?,
    val email: String,
    val tel: String?,
    val status: Status
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
