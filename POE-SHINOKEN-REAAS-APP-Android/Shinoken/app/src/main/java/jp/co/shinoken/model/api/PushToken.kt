package jp.co.shinoken.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PushToken(
    @Json(name = "device_id")
    val deviceId: String,
    val token: String,
    val kind: String = "android"
)