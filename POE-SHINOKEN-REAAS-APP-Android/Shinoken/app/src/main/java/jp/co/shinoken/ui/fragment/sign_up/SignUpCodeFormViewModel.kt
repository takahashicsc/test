package jp.co.shinoken.ui.fragment.sign_up

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.options.AuthSignInOptions
import com.amplifyframework.auth.result.AuthSignInResult
import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.model.AppError
import jp.co.shinoken.repository.AuthRepository
import jp.co.shinoken.repository.StoreRepository
import jp.co.shinoken.repository.UserInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class SignUpCodeFormViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    private val userInfoRepository: UserInfoRepository,
    private val storeRepository: StoreRepository,
    @Assisted savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val isButtonEnable: StateFlow<Boolean> get() = _isButtonEnable
    private val _isButtonEnable = MutableStateFlow(false)

    val isNotExistsUser: StateFlow<Boolean> get() = _isNotExistsUser
    private val _isNotExistsUser = MutableStateFlow(false)

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    val apiState: StateFlow<ApiState> get() = _apiState
    private val _apiState = MutableStateFlow(ApiState.Empty)

    var code: String = ""
    val userName: String = savedStateHandle.get<String>("userName") ?: ""
    val password: String = savedStateHandle.get<String>("password") ?: ""

    fun validation() {
        if (userName.isBlank() || password.isBlank()) {
            onAppError()
            return
        }
        _isButtonEnable.value = code.isNotBlank()
    }

    private fun onAppError() {
        _appError.value = AppError.ArgumentNotException
    }

    fun confirmSignUp() {
        _apiState.value = ApiState.LOADING
        _isButtonEnable.value = false
        viewModelScope.launch {
            val response = authRepository.confirmSignUp(
                userName = userName,
                confirmationCode = code
            )

            if (response.status == Status.SUCCESS) {
                signIn()
            } else {
                if(response.appError is AppError.CognitoException && response.appError.cause?.message == "Confirm sign up failed") {
                    signIn()
                } else {
                    _apiState.value = ApiState.Empty
                    _appError.value = response.appError
                }
            }
            validation()
        }
    }

    private suspend fun signIn() {
        val response = authRepository.signIn(
            userName = userName,
            password = password,
            options = AuthSignInOptions.defaults(),
        )
        if (response.status == Status.SUCCESS) {
            getUserInfo()
        } else {
            _apiState.value = ApiState.Empty
            _appError.value = response.appError
        }
    }

    private suspend fun getUserInfo() {
        val cognitoToken = authRepository.getCognitoToken().data ?: return
        val response = userInfoRepository.getUserInfo(cognitoToken)

        val uuid = storeRepository.getUUID()
        if (response.status == Status.SUCCESS && uuid.isNullOrEmpty().not()) {
            val userInfo = response.data!!
            if (userInfo.data.detectedAccountables.isNotEmpty()) {
                val tokenResponse =
                    userInfoRepository.authorizeExecute(
                        cognitoToken,
                        userInfo.data.detectedAccountables.first().authId,
                        uuid!!
                    )
                storeRepository.saveCurrentAccountId(userInfo.data.detectedAccountables.first().authId)
                if (tokenResponse.status == Status.SUCCESS) {
                    storeRepository.saveApiToken(tokenResponse.data!!.data.accessToken)
                    tokenResponse.data.data.refreshToken?.let {
                        storeRepository.saveRefreshToken(it)
                    }
                    _apiState.value = ApiState.Success
                } else {
                    _apiState.value = ApiState.Empty
                    _isNotExistsUser.value = true
                }
            } else {
                _apiState.value = ApiState.Empty
                _isNotExistsUser.value = true
            }
        } else {
            _apiState.value = ApiState.Empty
            _appError.value = response.appError
        }
    }

    fun resendSignUpCode() {
        _apiState.value = ApiState.LOADING
        viewModelScope.launch {
            val response = authRepository.resendSignUpCode(
                userName = userName,
            )

            if (response.status == Status.SUCCESS) {
                _apiState.value = ApiState.Empty
            } else {
                _apiState.value = ApiState.Empty
                _appError.value = response.appError
            }
        }
    }
}