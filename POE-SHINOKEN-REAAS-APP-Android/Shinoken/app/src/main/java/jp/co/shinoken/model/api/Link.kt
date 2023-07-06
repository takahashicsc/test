package jp.co.shinoken.model.api

import com.squareup.moshi.JsonClass
import java.io.Serializable

/*"kind": "tel",
"title": "電話で問い合わせ",
"url": "0120-000-000"
}*/
@JsonClass(generateAdapter = true)
data class Link(
    val kind: Kind,
    val title: String,
    val url: String,
): Serializable