package jp.co.shinoken.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Benefits(
    val total: Int,
    val data: List<Benefit>
)

/*
"id": 10,
"title": "特典123",
"slug": "bene123",
"start_at": "2021-02-05T12:05:57.000Z",
"end_at": "2031-02-15T14:55:57.000Z",
"content_text": null,
"images": [ ],
"labels": [ ],
"links": [ ],
"website_url": "https://reivo.co.jp"
 */
@JsonClass(generateAdapter = true)
data class Benefit(
    val id: Int,
    val title: String,
    val slug: String,
    @Json(name = "website_url")
    val url: String,
    @Json(name = "start_at")
    val startAt: String,
    var images: List<Image>? = null
)