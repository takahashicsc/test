package jp.co.shinoken.api.response

import com.squareup.moshi.JsonClass
import jp.co.shinoken.model.api.Image

@JsonClass(generateAdapter = true)
data class ResponseImage(
    val message: String,
    val data: Image?
)