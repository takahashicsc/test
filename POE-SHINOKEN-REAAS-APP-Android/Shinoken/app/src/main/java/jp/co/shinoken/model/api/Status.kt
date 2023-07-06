package jp.co.shinoken.model.api

import com.squareup.moshi.Json

enum class Status {
    @Json(name = "opened")
    Opened,

    @Json(name = "draft")
    Draft
}