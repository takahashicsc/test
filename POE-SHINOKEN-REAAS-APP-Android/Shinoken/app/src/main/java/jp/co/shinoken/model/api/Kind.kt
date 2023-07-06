package jp.co.shinoken.model.api

import com.squareup.moshi.Json

/*
* tel: 電話
* link: ブラウザを開く
* deeplink: アプリ内遷移
* */
enum class Kind {
    @Json(name = "tel")
    Tel,

    @Json(name = "link")
    Link,

    @Json(name = "deeplink")
    DeepLink
}