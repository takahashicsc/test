package jp.co.shinoken.api.aws

import com.amplifyframework.auth.AuthSession
import com.amplifyframework.auth.options.AuthSignInOptions
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.AuthResetPasswordResult
import com.amplifyframework.auth.result.AuthSignInResult
import com.amplifyframework.auth.result.AuthSignUpResult


interface AmplifyClient {
    suspend fun signUp(
        userName: String, password: String, options: AuthSignUpOptions,
    ): AuthSignUpResult

    suspend fun confirmSignUp(
        userName: String,
        confirmationCode: String,
    ): AuthSignUpResult

    suspend fun resendSignUp(
        userName: String,
    ): AuthSignUpResult

    suspend fun signIn(
        userName: String,
        password: String,
        options: AuthSignInOptions
    ): AuthSignInResult

    suspend fun resetPassword(
        userName: String,
    ): AuthResetPasswordResult

    suspend fun confirmResetPassword(
        newPassword: String,
        confirmationCode: String,
    )

    suspend fun updatePassword(
        oldPassword: String,
        newPassword: String,
    )

    suspend fun getCognitoToken(): String

    suspend fun fetchAuthSession(): AuthSession

    suspend fun signOut()
}