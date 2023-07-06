package jp.co.shinoken.model.api

import androidx.annotation.DrawableRes
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import jp.co.shinoken.R
import java.io.Serializable

/*
{
  "data": {
    "menus": [
      {
        "slug": "notice",
        "title": "お知らせ /掲示板",
        "subtitle": "新着12件",
        "new": true
      },
      {
        "slug": "bill",
        "title": "ご請求一覧",
        "subtitle": "2020/12",
        "new": true
      },
      {
        "slug": "procedure",
        "title": "各種お手続き",
        "subtitle": "進行中1件",
        "new": true
      },
      {
        "slug": "inquiry",
        "title": "お問い合わせ",
        "subtitle": "進行中1件",
        "new": true
      },
      {
        "slug": "faq",
        "title": "よくある質問",
        "subtitle": null,
        "new": false
      },
      {
        "slug": "manual",
        "title": "住まいのマニュアル",
        "subtitle": null,
        "new": false
      },
      {
        "slug": "benefit",
        "title": "入居者特典",
        "subtitle": null,
        "new": false
      },
      {
        "slug": "trash_calendar",
        "title": "ゴミの日通知",
        "subtitle": null,
        "new": false
      },
      {
        "slug": "media",
        "title": "シノケンメディア",
        "subtitle": null,
        "new": false
      }
    ],
    "me": {
      "auth_id": "Resident_15",
      "accountable_type": "Resident",
      "user_sub": "041fac994a3ddd7a",
      "name": "坂上田村麻呂e6af23fe",
      "name_phonetic": "サカノウエノタムラマロe6af23fe",
      "email": "rv-blackhole+041fac994a3ddd7a@reivo.co.jp",
      "tel": "000-0000-0000+041fac994a3ddd7a",
      "building": {
        "id": 10,
        "code": "899f53e3",
        "name": "福岡テスト建物マンション899f53e3",
        "prefecture_id": 40,
        "prefecture_name": "福岡県",
        "address": "812-8577"
      },
      "room": {
        "id": 28,
        "code": "899f53e3_203",
        "number": "203"
      },
      "icon": {
        "key": "l5clddndifiy41ualrsjgz9wl8mw",
        "content_type": "image/png",
        "publish_url": "https://s3-ap-northeast-1.amazonaws.com/shinoken-demo.reivo.info/public/shared/user/icon/l5clddndifiy41ualrsjgz9wl8mw",
        "variant_urls": {}
      },
      "contract": {
        "contract_start_at": "2016-11-01T00:00:00.000Z",
        "contract_end_at": "2090-10-20T00:00:00.000Z",
        "cancelled_at": null,
        "active": true,
        "roommates_count": 0
      },
      "check_form": {
        "submittable": false,
        "submitted_at": null,
        "status": "draft"
      },
      "lifelines": [
        {
          "name": "福岡水道局899f53e3支店",
          "tel": "0120-000-000"
        },
        {
          "name": "福岡ガスセンター899f53e3支店",
          "tel": "0120-000-000"
        }
      ],
      "detected_accountables": [
        {
          "id": 15,
          "auth_id": "Resident_15",
          "code": "e6af23fe",
          "name": "坂上田村麻呂e6af23fe",
          "residence_name": "福岡テスト建物マンション899f53e3 203",
          "contract_start_at": "2016-11-01T00:00:00.000Z",
          "contract_end_at": "2090-10-20T00:00:00.000Z"
        }
      ],
      "meta": {
        "terms_of_service": "https://shinoken.reivo.info/pages/terms-of-service",
        "privacy_policy": "https://shinoken.reivo.info/pages/privacy-policy",
        "mail_transfer": "https://www.post.japanpost.jp/service/tenkyo/index.html"
      }
    },
    "weather": {
      "updated_at": "2021-02-08T17:00:00+09:00",
      "area": "福岡地方",
      "weathers": [
        {
          "time": "2021-02-08T17:00:00+09:00",
          "time_name": "今夜",
          "title": "晴れ",
          "code": "100"
        },
        {
          "time": "2021-02-09T00:00:00+09:00",
          "time_name": "明日",
          "title": "晴れ",
          "code": "100"
        },
        {
          "time": "2021-02-10T00:00:00+09:00",
          "time_name": "明後日",
          "title": "晴れ時々くもり",
          "code": "101"
        }
      ],
      "precipitations": [
        {
          "time": "2021-02-08T18:00:00+09:00",
          "time_name": "１８時から００時まで",
          "value": "0"
        },
        {
          "time": "2021-02-09T00:00:00+09:00",
          "time_name": "００時から０６時まで",
          "value": "0"
        },
        {
          "time": "2021-02-09T06:00:00+09:00",
          "time_name": "０６時から１２時まで",
          "value": "0"
        },
        {
          "time": "2021-02-09T12:00:00+09:00",
          "time_name": "１２時から１８時まで",
          "value": "0"
        },
        {
          "time": "2021-02-09T18:00:00+09:00",
          "time_name": "１８時から２４時まで",
          "value": "0"
        }
      ],
      "temperatures": [
        {
          "time": "2021-02-09T00:00:00+09:00",
          "time_name": "明日朝",
          "title": "朝の最低気温",
          "value": "4",
          "slug": "min"
        },
        {
          "time": "2021-02-09T09:00:00+09:00",
          "time_name": "明日日中",
          "title": "日中の最高気温",
          "value": "10",
          "slug": "max"
        }
      ]
    },
    "suggestions": [
      {
        "type": "faq",
        "id": 8,
        "title": "test1",
        "slug": "test123455"
      },
      {
        "type": "faq",
        "id": 35,
        "title": "共用部に蜂の巣があります。",
        "slug": "8c67a1a2fc79a592"
      },
      {
        "type": "faq",
        "id": 36,
        "title": "廊下の共用灯が点灯していません。",
        "slug": "146d9e80ac85c74b"
      },
      {
        "type": "faq",
        "id": 37,
        "title": "退去月の家賃はどうなりますか。",
        "slug": "daf61c4fb45d3b59"
      },
      {
        "type": "faq",
        "id": 38,
        "title": "共用部に蜂の巣があります。",
        "slug": "19f5ce33f760f2d3"
      },
      {
        "type": "faq",
        "id": 39,
        "title": "廊下の共用灯が点灯していません。",
        "slug": "eb85f25f5576c4b4"
      }
    ]
  }
}*/
@JsonClass(generateAdapter = true)
data class HomeParent(
    val data: Home
)

@JsonClass(generateAdapter = true)
data class Home(
    val menus: List<Menu>,
    val me: Me,
    val weather: WeatherParent,
    val suggestions: List<Suggestion>
) : Serializable

@JsonClass(generateAdapter = true)
data class Menu(
    val slug: Slug,
    val title: String,
    @Json(name = "subtitle")
    val subTitle: String?,
    val new: Boolean
) : Serializable {
    enum class Slug(@DrawableRes val iconRes: Int) {
        @Json(name = "media")
        Media(R.drawable.ic_logo),

        @Json(name = "trash_calendar")
        TrashCalendar(R.drawable.ic_menu_icon8),

        @Json(name = "benefit")
        Benefit(R.drawable.ic_menu_icon7),

        @Json(name = "manual")
        Manual(R.drawable.ic_menu_icon6),

        @Json(name = "faq")
        Faq(R.drawable.ic_menu_icon5),

        @Json(name = "inquiry")
        Inquiry(R.drawable.ic_menu_icon4),

        @Json(name = "procedure")
        Procedure(R.drawable.ic_menu_icon3),

        @Json(name = "bill")
        Bill(R.drawable.ic_menu_icon2),

        @Json(name = "notice")
        Notice(R.drawable.ic_menu_icon1)
    }
}

@JsonClass(generateAdapter = true)
data class Suggestion(
    val type: Type,
    val id: Int,
    val title: String,
    val slug: String,
    val image: Image?
) : Serializable {
    enum class Type {
        @Json(name = "faq")
        Faq
    }
}

@JsonClass(generateAdapter = true)
data class WeatherParent(
    val area: String?,
    val weathers: List<Weather>,
    val precipitations: List<Precipitation>,
    val temperatures: List<Temperature>
)

@JsonClass(generateAdapter = true)
data class Weather(
    val time: String,
    @Json(name = "time_name")
    val timeName: String,
    val title: String,
    val code: String,
    @Json(name = "group_code")
    val groupCode: GroupCode,
) {
    enum class GroupCode(@DrawableRes val iconRes: Int) {
        @Json(name = "100")
        Sunny(R.drawable.ic_icon_sun),

        @Json(name = "200")
        Cloudy(R.drawable.ic_icon_cloud),

        @Json(name = "300")
        Rain(R.drawable.ic_icon_rain),

        @Json(name = "400")
        Snow(R.drawable.ic_icon_snow),
    }
}

@JsonClass(generateAdapter = true)
data class Precipitation(
    val time: String,
    @Json(name = "time_name")
    val timeName: String,
    val value: String,
)

@JsonClass(generateAdapter = true)
data class Temperature(
    val time: String,
    @Json(name = "time_name")
    val timeName: String,
    val title: String,
    val value: String,
    val slug: String,
)

