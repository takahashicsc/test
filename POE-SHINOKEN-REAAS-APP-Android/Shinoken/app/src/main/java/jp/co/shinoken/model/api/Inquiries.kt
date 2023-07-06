package jp.co.shinoken.model.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Inquiries(
    val kinds: List<Kind>,
    val total: Int,
    val data: List<Inquiry>
) {
    @JsonClass(generateAdapter = true)
    data class Kind(
        val slug: String,
        val title: String
    )
}

@JsonClass(generateAdapter = true)
data class ResponseSuccessInquiry(
    val message: String,
    val data: Inquiry
)

@JsonClass(generateAdapter = true)
data class InquiryDetail(
    val data: Inquiry
)

/*
"id": 10,
"kind": "shared_space",
"email": "rv-blackhole+041fac994a3ddd7a@reivo.co.jp",
"body": "I have a dream.",
"ip": "::1",
"created_at": "2021-02-13T00:56:52.954Z"
 */
@JsonClass(generateAdapter = true)
data class Inquiry(
    val id: Int,
    val kind: String,
    val email: String,
    val body: String,
    val ip: String,
)