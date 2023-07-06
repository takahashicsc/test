package jp.co.shinoken.api.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestSignInSupport(
    val name: String,
    @Json(name = "name_phonetic")
    val nameKana: String,
    @Json(name = "tel")
    val phoneNumber: String,
    val email: String,
    val body: String,
    // FIXME: 直指定で良いか
    val kind: String = "others",
)