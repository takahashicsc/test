package jp.co.shinoken.model.api

import com.squareup.moshi.JsonClass

/**
 * "success": true,
"code": "app_version_ok",
"message": "",
"version": "2.1.0",
"data": null
 */
@JsonClass(generateAdapter = true)
data class VersionCheck(
    val success: Boolean,
    val code: String,
    val message: String,
    val version: String
)