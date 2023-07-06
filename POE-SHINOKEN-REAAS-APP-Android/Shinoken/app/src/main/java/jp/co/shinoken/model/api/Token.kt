package jp.co.shinoken.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TokenRefresh(
    val data: Token
)

@JsonClass(generateAdapter = true)
data class Token(
    @Json(name = "access_token")
    val accessToken: String,

    @Json(name = "refresh_token")
    val refreshToken: String?
)