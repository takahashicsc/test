package jp.co.shinoken.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Image(
    val key: String,
    @Json(name = "content_type")
    val contentType: String,
    @Json(name = "publish_url")
    val publishUrl: String,
    @Json(name = "variant_urls")
    val variantUrls: VariantUrl?
) : Serializable