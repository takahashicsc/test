package jp.co.shinoken.repository

import com.amplifyframework.auth.options.AuthSignInOptions
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.AuthResetPasswordResult
import com.amplifyframework.auth.result.AuthSignInResult
import com.amplifyframework.auth.result.AuthSignUpResult
import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.request.RequestSignInSupport
import jp.co.shinoken.model.api.ResponseSuccessInquiry

interface AuthRepository {
    suspend fun signIn(
        userName: String,
        password: String,
        options: AuthSignInOptions,
    ): ApiResponse<AuthSignInResult>

    suspend fun signUp(
        userName: String,
        password: String,
        options: AuthSignUpOptions,
    ): ApiResponse<AuthSignUpResult>

    suspend fun resendSignUpCode(
        userName: String,
    ): ApiResponse<AuthSignUpResult>

    suspend fun confirmSignUp(
        userName: String,
        confirmationCode: String,
    ): ApiResponse<AuthSignUpResult>

    suspend fun resetPassword(
        userName: String,
    ): ApiResponse<AuthResetPasswordResult>

    suspend fun confirmResetPassword(
        newPassword: String,
        confirmationCode: String,
    ): ApiResponse<Unit>

    suspend fun updatePassword(
        oldPassword: String,
        newPassword: String,
    ): ApiResponse<Unit>

    suspend fun getCognitoToken(): ApiResponse<String>

    suspend fun fetchAuthSession(): ApiResponse<Boolean>

    suspend fun signOut(): ApiResponse<Unit>

    suspend fun postSignInSupport(requestSignInSupport: RequestSignInSupport): ApiResponse<ResponseSuccessInquiry>
}