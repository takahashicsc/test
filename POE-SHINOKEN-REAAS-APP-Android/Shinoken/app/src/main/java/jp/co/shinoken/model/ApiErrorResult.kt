package jp.co.shinoken.model

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody

object ApiErrorResult {

    fun parseAlertError(response: ResponseBody): AlertError {
        val errorBody = response.string()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return try {
            return moshi.adapter(AlertError::class.java).fromJson(errorBody)!!
        } catch (e: Exception) {
            AlertError(
                code = "",
                message = "通信に失敗しました。\n(E8888)"
            )
        }
    }

    fun parseValidationError(response: ResponseBody): AlertError {
        val errorBody = response.string()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return try {
            val validationError = moshi.adapter(ValidationError::class.java).fromJson(errorBody)!!
            AlertError(
                validationError.code,
                validationError.errors.joinToString("\n"),
                "E0030"
            )
        } catch (e: Exception) {
            parseAlertError(response)
        }
    }
}