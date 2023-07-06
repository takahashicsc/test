package jp.co.shinoken.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import jp.co.shinoken.extension.convertDateToString
import jp.co.shinoken.extension.convertStringToDate
import java.io.Serializable
import java.time.DateTimeException
import java.util.*


@JsonClass(generateAdapter = true)
data class Boards(
    val total: Int,
    val data: List<Board>
)

@JsonClass(generateAdapter = true)
data class BoardDetail(
    val data: Board
)


/*
  "data": {
    "id": 11,
    "title": "掲示板テスト記事board6668c8b5",
    "slug": "c246c1a5f07b7f2c",
    "start_at": "2021-03-08T11:30:11.000Z",
    "end_at": "2031-03-08T11:30:11.000Z",
    "content_text": "<p>○月○日町内会行事中止のお知らせ 毎年開催してきた本行事は、新型コロナウイルス感染拡大防止のため、今年度は残念ながら中止とさせていただきます。</p>",
    "images": [],
    "labels": [],
    "links": [
      {
        "kind": "deeplink",
        "title": "お客様のマイページ",
        "url": "shinoken-residentapp-demo:///mypage"
      },
      {
        "kind": "deeplink",
        "title": "利用規約",
        "url": "shinoken-residentapp-demo:///page/terms-of-service"
      },
      {
        "kind": "link",
        "title": "シノケン企業概要",
        "url": "https://www.shinoken.co.jp/about/company/"
      },
      {
        "kind": "tel",
        "title": "電話で問い合わせ",
        "url": "0120-000-000"
      },
      {
        "kind": "tel",
        "title": "電話で問い合わせ",
        "url": "0120-000-000"
      },
      {
        "kind": "link",
        "title": "シノケン企業概要",
        "url": "https://www.shinoken.co.jp/about/company/"
      }
    ]
  }
 */

@JsonClass(generateAdapter = true)
data class Board(
    val id: Int,
    val title: String,
    val slug: String,
    @Json(name = "start_at")
    val startAt: String?,
    @Json(name = "content_text")
    val contentText: String?,
) : Serializable {
    fun getStartAtFormatString(): String? {
        startAt ?: return null
        val startAtDate = startAt.convertStringToDate()
        return startAtDate?.convertDateToString("yyyy.MM.dd")
    }
}