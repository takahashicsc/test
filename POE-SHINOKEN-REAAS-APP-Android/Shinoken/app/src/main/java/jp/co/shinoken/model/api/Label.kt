package jp.co.shinoken.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/*
{
"id": 8,
"name": "設備",
"path": "/faq/cat/facility",
"image": null,
"status": "opened",
"serial_code": 0
}
 */

@JsonClass(generateAdapter = true)
data class Label(
    val id: Int,
    val name: String,
    val path: String,
    val image: Image?,
    val status: Status,
    @Json(name = "serial_code")
    val serialCode: Int
)
