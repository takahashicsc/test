package jp.co.shinoken.model.api

import com.squareup.moshi.JsonClass
import java.io.Serializable

/*
"thumbnail": "https://s3-ap-northeast-1.amazonaws.com/shinoken-demo.reivo.info/public/shared/user/check_form/1bufuiucrh3g9z287bqhb72c3mwg.thumbnail",
"original": "https://s3-ap-northeast-1.amazonaws.com/shinoken-demo.reivo.info/public/shared/user/check_form/1bufuiucrh3g9z287bqhb72c3mwg.original"
 */
@JsonClass(generateAdapter = true)
data class VariantUrl(
    val thumbnail: String?,
    val original: String?
): Serializable