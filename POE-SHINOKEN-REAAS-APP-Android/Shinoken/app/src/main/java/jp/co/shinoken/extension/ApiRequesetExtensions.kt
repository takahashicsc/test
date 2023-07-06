package jp.co.shinoken.extension

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

val String.convertRequestBody: RequestBody
    get() = this.toRequestBody(MultipartBody.FORM)

val File?.convertFileRequestBody: MultipartBody.Part
    get() {
        val requestBody = this?.asRequestBody() ?: "".toRequestBody(MultipartBody.FORM)
        return MultipartBody.Part.createFormData("image", this?.name, requestBody)
    }

val File?.convertImageRequestBody: MultipartBody.Part
    get() {
        val requestBody = this?.asRequestBody() ?: "".toRequestBody(MultipartBody.FORM)
        return MultipartBody.Part.createFormData("file", this?.name, requestBody)
    }