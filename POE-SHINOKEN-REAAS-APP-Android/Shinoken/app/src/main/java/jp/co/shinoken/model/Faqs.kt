package jp.co.shinoken.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import jp.co.shinoken.model.api.Image
import jp.co.shinoken.model.api.Link
import jp.co.shinoken.model.api.Status
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Faqs(
    val categories: List<FaqCategory>,
    val total: Int,
    val data: List<FaqContent>
)

@JsonClass(generateAdapter = true)
data class FaqDetail(
    val data: FaqContent
)

/*"id": 5,
"name": "cat1",
"path": "/faq/cat/cat1",
"image": null,
"status": "opened",
"serial_code": 0*/
@JsonClass(generateAdapter = true)
data class FaqCategory(
    val id: Int,
    val name: String,
    val path: String,
    val image: Image?,
    val status: Status,
    @Json(name = "serial_code")
    val serialCode: Int,
    val isSelected: Boolean = false
): Serializable


/*
"id": 45,
"title": "廊下の共用灯が点灯していません。",
"slug": "0ca770ac41dff766",
"start_at": "2021-03-04T08:36:55.000Z",
"end_at": "2121-03-04T08:36:55.000Z",
"content_text": "ガスメーターの安全装置がはたらき、ガスの供給が...",
"images": [ ],
"labels": [],
"links": [ ]
*/
@JsonClass(generateAdapter = true)
data class FaqContent(
    val id: Int,
    val title: String,
    val slug: String,
    @Json(name = "content_text")
    val contentText: String,
    val images: List<Image>,
    val categories: List<FaqCategory>,
    val links: List<Link>
): Serializable

/*
* tel: 電話
* link: ブラウザを開く
* deeplink: アプリ内遷移
* */
enum class FaqKind: Serializable {
    @Json(name = "tel")
    Tel,

    @Json(name = "link")
    Link,

    @Json(name = "deeplink")
    DeepLink
}