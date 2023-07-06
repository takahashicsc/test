package jp.co.shinoken.api.request

import com.squareup.moshi.JsonClass

/*
"email": "test+50911d2b@example.com",
"name": "同居人A",
"birthday": "2000-10-10"
 */
@JsonClass(generateAdapter = true)
data class RequestRoomMate(
    val email: String,
    val name: String,
    val birthday: String
)