package jp.co.shinoken.api.aws.imple

import com.amazonaws.mobile.client.*
import com.amazonaws.mobile.client.results.Tokens
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthSession
import com.amplifyframework.auth.options.AuthSignInOptions
import com.amplifyframework.auth.options.AuthSignOutOptions
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.AuthResetPasswordResult
import com.amplifyframework.auth.result.AuthSignInResult
import com.amplifyframework.auth.result.AuthSignUpResult
import com.amplifyframework.kotlin.core.Amplify
import jp.co.shinoken.api.CognitoTokenException
import jp.co.shinoken.api.aws.AmplifyClient
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AmplifyService @Inject constructor() : AmplifyClient {
    override suspend fun signUp(
        userName: String,
        password: String,
        options: AuthSignUpOptions,
    ): AuthSignUpResult {
        return try {
            Amplify.Auth.signUp(
                userName,
                password,
                options
            )
        } catch (exception: AuthException.UsernameExistsException) {
            Amplify.Auth.resendSignUpCode(
                userName
            )
        }
    }

    override suspend fun confirmSignUp(
        userName: String,
        confirmationCode: String,
    ): AuthSignUpResult {
        return Amplify.Auth.confirmSignUp(
            userName,
            confirmationCode
        )
    }

    override suspend fun resendSignUp(userName: String): AuthSignUpResult {
        return Amplify.Auth.resendSignUpCode(
            userName
        )
    }

    override suspend fun signIn(
        userName: String,
        password: String,
        options: AuthSignInOptions,
    ): AuthSignInResult {
        return Amplify.Auth.signIn(
            userName,
            password,
            options
        )
    }

    override suspend fun resetPassword(
        userName: String,
    ): AuthResetPasswordResult {
        return Amplify.Auth.resetPassword(
            userName
        )
    }

    override suspend fun confirmResetPassword(
        newPassword: String,
        confirmationCode: String,
    ) {
        return Amplify.Auth.confirmResetPassword(
            newPassword,
            confirmationCode
        )
    }

    override suspend fun updatePassword(
        oldPassword: String,
        newPassword: String
    ) {
        return Amplify.Auth.updatePassword(
            oldPassword,
            newPassword,
        )
    }

    override suspend fun getCognitoToken(): String {
        fun getTokenCallback(onSuccess: (String) -> Unit, onError: (Exception) -> Unit) {
            AWSMobileClient.getInstance()
                .currentUserState(object : Callback<UserStateDetails> {
                    override fun onResult(result: UserStateDetails?) {
                        when (result?.userState) {
                            UserState.SIGNED_IN -> {
                                AWSMobileClient.getInstance()
                                    .getTokens(object : Callback<Tokens> {
                                        override fun onResult(result: Tokens) {
                                            onSuccess.invoke(result.accessToken.tokenString)
                                        }

                                        override fun onError(e: Exception) {
                                            onError.invoke(CognitoTokenException())
                                        }
                                    })
                            }
                            else -> onError.invoke(CognitoTokenException())
                        }
                    }

                    override fun onError(e: Exception) {
                        onError.invoke(CognitoTokenException())
                    }
                })
        }

        return suspendCoroutine { cont ->
            getTokenCallback({ token ->
                cont.resume(token)
            }, {
                cont.resumeWithException(it)
            })
        }
    }

    override suspend fun fetchAuthSession(): AuthSession {
        return Amplify.Auth.fetchAuthSession()
    }

    override suspend fun signOut() {
        return Amplify.Auth.signOut(
            AuthSignOutOptions.builder()
                .globalSignOut(true)
                .build()
        )
    }
}