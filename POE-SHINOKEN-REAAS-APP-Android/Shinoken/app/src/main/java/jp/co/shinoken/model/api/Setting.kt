package jp.co.shinoken.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Setting(
    @Json(name = "allowed_subresident_to_check_billing")
    val allowedSubresidentToCheckBilling: Boolean
)

@JsonClass(generateAdapter = true)
data class Settings(
    val data: Setting
)

@JsonClass(generateAdapter = true)
data class ResponseSettings(
    val message: String,
    val data: Setting
)