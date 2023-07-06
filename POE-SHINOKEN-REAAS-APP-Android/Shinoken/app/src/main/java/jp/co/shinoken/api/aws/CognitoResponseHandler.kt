package jp.co.shinoken.api.aws

import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.AuthException
import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.CognitoTokenException
import jp.co.shinoken.model.AppError

class CognitoResponseHandler {
    fun <T : Any> handleSuccess(data: T): ApiResponse<T> {
        return ApiResponse.success(data)
    }

    fun <T : Any> handleException(e: Exception): ApiResponse<T> {
        return ApiResponse.error(
            if (e is AmplifyException) {
                when (e) {
                    is AuthException -> {
                        when (e) {
                            is AuthException.InvalidParameterException -> {
                                AppError.CognitoException(
                                    e,
                                    CognitoErrorMessages.InvalidParameterException
                                )
                            }

                            is AuthException.InvalidPasswordException -> {
                                AppError.CognitoException(
                                    e,
                                    CognitoErrorMessages.InvalidPasswordException
                                )
                            }

                            is AuthException.CodeMismatchException -> {
                                AppError.CognitoException(
                                    e,
                                    CognitoErrorMessages.CodeMismatchException
                                )
                            }

                            is AuthException.LimitExceededException -> {
                                AppError.CognitoException(
                                    e,
                                    CognitoErrorMessages.LimitExceededException
                                )
                            }

                            is AuthException.CodeExpiredException -> {
                                AppError.CognitoException(
                                    e,
                                    CognitoErrorMessages.CodeExpiredException
                                )
                            }

                            is AuthException.UserNotFoundException -> {
                                AppError.CognitoException(
                                    e,
                                    CognitoErrorMessages.NotAuthorizedException
                                )
                            }

                            is AuthException.SessionExpiredException -> {
                                AppError.ApiException.AuthError
                            }

                            else -> {
                                if(e.message == "Sign in failed") {
                                    AppError.CognitoException(
                                        e,
                                        CognitoErrorMessages.NotAuthorizedException
                                    )
                                } else {
                                    AppError.CognitoException(
                                        e,
                                        CognitoErrorMessages.OtherException
                                    )
                                }
                            }
                        }
                    }
                    else -> {
                        AppError.CognitoException(e, CognitoErrorMessages.OtherException)
                    }
                }
            } else {
                if (e is CognitoTokenException) {
                    AppError.ApiException.AuthError
                } else {
                    AppError.CognitoException(e, CognitoErrorMessages.OtherException)
                }
            }
        )
    }
}