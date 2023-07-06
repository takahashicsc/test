package jp.co.shinoken.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Asset(
    val key: String,
    @Json(name = "content_type")
    val contentType: String,
    @Json(name = "publish_url")
    val url: String
)