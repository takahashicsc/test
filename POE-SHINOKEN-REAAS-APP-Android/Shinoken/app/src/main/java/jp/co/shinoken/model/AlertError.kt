package jp.co.shinoken.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlertError(
    val code: String,
    val message: String,
    val errorMessageSuffixCode: String ="E0040"
)