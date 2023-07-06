package jp.co.shinoken.api.response

import com.squareup.moshi.JsonClass
import jp.co.shinoken.model.api.Icon

@JsonClass(generateAdapter = true)
data class ResponseIcon(
    val message: String,
    val data: Icon?
)