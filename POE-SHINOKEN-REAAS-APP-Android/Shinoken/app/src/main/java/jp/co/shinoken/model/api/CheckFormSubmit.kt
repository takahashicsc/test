package jp.co.shinoken.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckFormSubmit(
    @Json(name = "repairment_required")
    val repairmentRequired: Boolean
)