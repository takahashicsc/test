package jp.co.shinoken.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import jp.co.shinoken.extension.convertDateToString
import jp.co.shinoken.extension.convertStringToDate
import java.io.Serializable
import java.time.DateTimeException

/*
"total": 42,
"unread_count": 42,
"data"
 */

@JsonClass(generateAdapter = true)
data class Notifications(
    val total: Int,
    @Json(name = "unread_count")
    val unreadCount: Int,
    val data: List<Notification>
)

@JsonClass(generateAdapter = true)
data class NotificationDetail(
    val data: Notification
)

/*
"id": 46,
"notification_publishment_id": 7,
"title": "リニューアルのお知らせ ntfcd131d50",
"content_text": "<p>平素は格別のご愛顧を賜り、厚くお礼申し上げます。 誠に勝手ながら○月○日（○）～○月○日（○）まで連休とさせていただきます。</p><p>なお、設備の故障など緊急のお問い合わせに関しましては、何卒ご理解いただきますよう順次回答させて頂きます。</p>",
"published_at": null,
"read": false,
"images": [ ],
"labels": [ ],
"links": [],
 */

@JsonClass(generateAdapter = true)
data class Notification(
    val id: Int,
    @Json(name = "notification_publishment_id")
    val notificationPublishId: Int,
    val title: String,
    @Json(name = "content_text")
    val contentText: String,
    @Json(name = "segmented_at")
    val segmentedAt: String?,
    val read: Boolean,
    val images: List<Image>,
    val labels: List<Label>,
    val links: List<Link>
) : Serializable {
    fun getSegmentedAtFormatString(): String? {
        segmentedAt ?: return null
        val segmentedAtDate = segmentedAt.convertStringToDate() ?: throw DateTimeException("")
        return segmentedAtDate.convertDateToString("yyyy.MM.dd")
    }
}