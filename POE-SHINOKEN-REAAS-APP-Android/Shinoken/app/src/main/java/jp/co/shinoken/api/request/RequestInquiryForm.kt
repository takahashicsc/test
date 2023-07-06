package jp.co.shinoken.api.request

import com.squareup.moshi.JsonClass

/*
"kind": "shared_space",
"body": "I have a dream."
 */
@JsonClass(generateAdapter = true)
data class RequestInquiryForm(
    val kind: String,
    val email: String,
    val tel: String?,
    val body: String,
    val name: String,
    val residence_name: String,
    val room_number: String,
    val office_name: String?,
)
