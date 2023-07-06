package jp.co.shinoken.api

import com.squareup.moshi.JsonDataException
import jp.co.shinoken.model.ApiErrorResult
import jp.co.shinoken.model.AppError
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ResponseHandler {
    fun <T : Any> handleSuccess(data: T): ApiResponse<T> {
        return ApiResponse.success(data)
    }

    fun <T : Any> handleException(e: Exception): ApiResponse<T> {
        return when (e) {
            is HttpException -> {
                val errorResponse = e.response()?.errorBody()
                    ?: return ApiResponse.error(AppError.ApiException.ApiOtherErrors(e))
                ApiResponse.error(
                    when (e.code()) {
                        400 -> {
                            AppError.ApiException.AlertAppError(
                                e,
                                ApiErrorResult.parseValidationError(errorResponse)
                            )
                        }
                        401 -> {
                            AppError.ApiException.AuthError
                        }
                        else -> {
                            AppError.ApiException.AlertAppError(
                                e,
                                ApiErrorResult.parseAlertError(errorResponse)
                            )
                        }
                    }
                )
            }
            is SocketTimeoutException, is UnknownHostException -> {
                ApiResponse.error(AppError.ApiException.UnKnownHostError(e))
            }
            is JsonDataException -> ApiResponse.error(AppError.ApiException.JsonDataException(e))

            else -> {
                ApiResponse.error(AppError.ApiException.ApiOtherErrors(e))
            }
        }
    }
}