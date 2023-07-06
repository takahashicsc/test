package jp.co.shinoken.model.api

import com.squareup.moshi.Json

data class Pages(
    val data: Page
)

/*
"data": {
    "id": 34,
    "title": "利用規約",
    "slug": "terms-of-service",
    "start_at": "2020-03-08T11:30:12.000Z",
    "end_at": "2041-03-08T11:30:12.000Z",
    "content_text": "<h2>利用規約</h2>\n<p>terms-of-service</p>\n<h3><span>免責事項</span></h3>\n<p>ウィキペディアが提供する素材については、合法性（第三者権利の非侵害性を含む）、内容の正確性、安全性等、いかなる保証もされません。<a href=\"/wiki/Wikipedia:%E5%88%A9%E7%94%A8%E8%80%85\" title=\"Wikipedia:利用者\">本サイトの利用者</a>、<a href=\"/wiki/Wikipedia:%E7%AE%A1%E7%90%86%E8%80%85\" title=\"Wikipedia:管理者\">管理者</a>、<a href=\"https://meta.wikimedia.org/wiki/System_administrators/ja\" class=\"extiw\" title=\"meta:System administrators/ja\">システム管理者</a>、<a href=\"/wiki/%E3%82%A6%E3%82%A3%E3%82%AD%E3%83%A1%E3%83%87%E3%82%A3%E3%82%A2%E8%B2%A1%E5%9B%A3\" title=\"ウィキメディア財団\">ウィキメディア財団</a>は、これらの素材が二次利用されることによって生ずるいかなる<a href=\"/wiki/%E6%90%8D%E5%AE%B3\" title=\"損害\">損害</a>に対しても一切責任を負いません。詳細は<a href=\"/wiki/Wikipedia:%E5%85%8D%E8%B2%AC%E4%BA%8B%E9%A0%85\" title=\"Wikipedia:免責事項\">免責事項</a>をお読みいただき、ご理解いただきますようお願いします。\n<ul>\n  <li>項目１</li>\n  <li>項目２</li>\n  <li>項目３</li>\n</ul>\n",
    "images": [ ],
    "labels": [ ],
    "links": [ ],
    "allow_public_access": true
}
 */
data class Page(
    val id: Int,
    val title: String,
    val slug: String,
    @Json(name = "content_text")
    val contentText: String,
    @Json(name = "allow_public_access")
    val isPublicAcces: Boolean
)
