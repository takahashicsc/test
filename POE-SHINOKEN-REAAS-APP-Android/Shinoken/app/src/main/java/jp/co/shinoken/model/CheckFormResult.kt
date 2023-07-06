package jp.co.shinoken.model

import com.squareup.moshi.Json
import java.io.Serializable

enum class CheckFormResult : Serializable {
    @Json(name = "ok")
    OK,

    @Json(name = "ng")
    NG
}