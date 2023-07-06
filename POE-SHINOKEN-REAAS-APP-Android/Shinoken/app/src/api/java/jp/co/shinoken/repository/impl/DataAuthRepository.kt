package jp.co.shinoken.repository.impl

import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.options.AuthSignInOptions
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.AuthResetPasswordResult
import com.amplifyframework.auth.result.AuthSignInResult
import com.amplifyframework.auth.result.AuthSignUpResult
import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.ResponseHandler
import jp.co.shinoken.api.ShinokenBootApiService
import jp.co.shinoken.api.aws.AmplifyClient
import jp.co.shinoken.api.aws.CognitoResponseHandler
import jp.co.shinoken.api.request.RequestSignInSupport
import jp.co.shinoken.model.api.ResponseSuccessInquiry
import jp.co.shinoken.repository.AuthRepository
import java.lang.Exception
import javax.inject.Inject

class DataAuthRepository @Inject constructor(
    private val amplifyClient: AmplifyClient,
    private val bootApiService: ShinokenBootApiService
) : AuthRepository {
    private val cognitoHandler = CognitoResponseHandler()
    private val responseHandler = ResponseHandler()
    override suspend fun signIn(
        userName: String,
        password: String,
        options: AuthSignInOptions,
    ): ApiResponse<AuthSignInResult> {
        return try {
            cognitoHandler.handleSuccess(amplifyClient.signIn(userName, password, options))
        } catch (exception: AmplifyException) {
            cognitoHandler.handleException(exception)
        }
    }

    override suspend fun signUp(
        userName: String,
        password: String,
        options: AuthSignUpOptions,
    ): ApiResponse<AuthSignUpResult> {
        return try {
            cognitoHandler.handleSuccess(amplifyClient.signUp(userName, password, options))
        } catch (exception: AuthException) {
            cognitoHandler.handleException(exception)
        }
    }

    override suspend fun resendSignUpCode(
        userName: String,
    ): ApiResponse<AuthSignUpResult> {
        return try {
            cognitoHandler.handleSuccess(amplifyClient.resendSignUp(userName))
        } catch (exception: AuthException) {
            cognitoHandler.handleException(exception)
        }
    }

    override suspend fun confirmSignUp(
        userName: String,
        confirmationCode: String,
    ): ApiResponse<AuthSignUpResult> {
        return try {
            cognitoHandler.handleSuccess(
                amplifyClient.confirmSignUp(userName, confirmationCode)
            )
        } catch (exception: AuthException) {
            cognitoHandler.handleException(exception)
        }
    }

    override suspend fun resetPassword(
        userName: String,
    ): ApiResponse<AuthResetPasswordResult> {
        return try {
            cognitoHandler.handleSuccess(
                amplifyClient.resetPassword(userName)
            )
        } catch (exception: AuthException) {
            cognitoHandler.handleException(exception)
        }
    }

    override suspend fun confirmResetPassword(
        newPassword: String,
        confirmationCode: String,
    ): ApiResponse<Unit> {
        return try {
            cognitoHandler.handleSuccess(
                amplifyClient.confirmResetPassword(newPassword, confirmationCode)
            )
        } catch (exception: AuthException) {
            cognitoHandler.handleException(exception)
        }
    }

    override suspend fun updatePassword(
        oldPassword: String,
        newPassword: String,
    ): ApiResponse<Unit> {
        return try {
            cognitoHandler.handleSuccess(
                amplifyClient.updatePassword(oldPassword, newPassword)
            )
        } catch (exception: AuthException) {
            cognitoHandler.handleException(exception)
        }
    }

    override suspend fun getCognitoToken(): ApiResponse<String> {
        return try {
            cognitoHandler.handleSuccess(
                amplifyClient.getCognitoToken()
            )
        } catch (exception: Exception) {
            cognitoHandler.handleException(exception)
        }
    }

    override suspend fun fetchAuthSession(): ApiResponse<Boolean> {
        return try {
            cognitoHandler.handleSuccess(
                amplifyClient.fetchAuthSession().isSignedIn
            )
        } catch (exception: AuthException) {
            cognitoHandler.handleException(exception)
        }
    }

    override suspend fun signOut(): ApiResponse<Unit> {
        return try {
            cognitoHandler.handleSuccess(
                amplifyClient.signOut()
            )
        } catch (exception: AuthException) {
            cognitoHandler.handleException(exception)
        }
    }

    override suspend fun postSignInSupport(requestSignInSupport: RequestSignInSupport): ApiResponse<ResponseSuccessInquiry> {
        return try {
            responseHandler.handleSuccess(
                bootApiService.postSignInSupport(requestSignInSupport)
            )
        } catch (exception: Exception) {
            responseHandler.handleException(exception)
        }
    }

}