package jp.co.shinoken.model.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Manuals(
    val total: Int,
    val data: List<Manual>
)

/*
{
    "id": 23,
    "title": "マニュアルman84f985aa",
    "slug": "man84f985aa",
    "start_at": "2021-03-08T11:30:12.000Z",
    "end_at": "2031-03-08T11:30:12.000Z",
    "content_text": null,
    "images": [ ],
    "labels": [ ],
    "links": [ ],
    "asset": {
        "key": "assetsman84f985aa",
        "content_type": "application/pdf",
        "publish_url": "https://s3-ap-northeast-1.amazonaws.com/shinoken-demo.reivo.info/public/shared/assetsman84f985aa",
        "variant_urls": { }
    }
},
 */
@JsonClass(generateAdapter = true)
data class Manual(
    val id: Int,
    val title: String,
    val asset: Asset
)