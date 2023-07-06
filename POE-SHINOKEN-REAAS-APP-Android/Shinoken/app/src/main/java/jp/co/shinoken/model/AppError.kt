package jp.co.shinoken.model

import jp.co.shinoken.api.aws.CognitoErrorMessages

sealed class AppError(cause: Throwable? = null) : RuntimeException(cause) {

    sealed class ApiException(cause: Throwable? = null) : AppError(cause) {

        data class UnKnownHostError(override val cause: Throwable?) : ApiException(cause)

        data class JsonDataException(override val cause: Throwable?) : ApiException(cause)

        data class AlertAppError(override val cause: Throwable?, val alertError: AlertError) :
            ApiException(cause)

        object AuthError : ApiException()

        data class ApiOtherErrors(override val cause: Throwable?) : ApiException(cause)
    }

    data class CognitoException(
        override val cause: Throwable?,
        val cognitoErrorMessages: CognitoErrorMessages
    ) : AppError(cause)

    object UserIdNotExistsError : AppError()

    object ArgumentNotException : AppError()

    object UnknownException : AppError()
}