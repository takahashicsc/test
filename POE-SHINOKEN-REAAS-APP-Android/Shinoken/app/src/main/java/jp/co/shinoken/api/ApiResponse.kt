package jp.co.shinoken.api

import jp.co.shinoken.model.AppError

data class ApiResponse<out T>(val status: Status, val data: T?, val appError: AppError?) {
    companion object {
        fun <T> success(data: T?): ApiResponse<T> {
            return ApiResponse(Status.SUCCESS, data, null)
        }

        fun <T> error(appError: AppError): ApiResponse<T> {
            return ApiResponse(Status.ERROR, null, appError)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR
}