package jp.co.shinoken.api.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestRoomMateCancel(
    val code: String
)