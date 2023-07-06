package jp.co.shinoken.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class IconParent(
    val data: Icon
)

@JsonClass(generateAdapter = true)
data class Icon(
    val key: String?,
    val contentType: String?,
    @Json(name = "publish_url")
    val publishUrl: String,
    @Json(name = "variant_urls")
    val variantUrl: VariantUrl?
) : Serializable