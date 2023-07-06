package jp.co.shinoken.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ValidationError(
    val code: String,
    val errors: List<String>)