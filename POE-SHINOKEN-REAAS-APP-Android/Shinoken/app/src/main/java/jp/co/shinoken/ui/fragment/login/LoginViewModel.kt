package jp.co.shinoken.ui.fragment.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.options.AuthSignInOptions
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.model.AppError
import jp.co.shinoken.repository.AuthRepository
import jp.co.shinoken.repository.MetaRepository
import jp.co.shinoken.repository.StoreRepository
import jp.co.shinoken.repository.UserInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    private val userInfoRepository: UserInfoRepository,
    private val storeRepository: StoreRepository,
    private val metaRepository: MetaRepository
) :
    ViewModel() {

    val isButtonEnable: StateFlow<Boolean> get() = _isButtonEnable
    private val _isButtonEnable = MutableStateFlow(false)

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    val apiState: StateFlow<ApiState> get() = _apiState
    private val _apiState = MutableStateFlow(ApiState.Empty)

    val isNotExistsUser: StateFlow<Boolean> get() = _isNotExistsUser
    private val _isNotExistsUser = MutableStateFlow(false)

    val termsUrl: StateFlow<String?> get() = _termsUrl
    private val _termsUrl = MutableStateFlow<String?>(null)

    var email: String = ""
    var password: String = ""

    fun fetch() {
        _appError.value = null
        getMetaData()
    }

    private fun getMetaData() {
        viewModelScope.launch {
            _apiState.value = ApiState.LOADING
            val response = metaRepository.getMeta()

            if (response.status == Status.SUCCESS) {
                _apiState.value = ApiState.Empty
                _termsUrl.value = response.data!!.data.termOfService
            } else {
                _apiState.value = ApiState.Empty
            }
        }
    }

    fun validation() {
        _isButtonEnable.value =
            email.isNotBlank() && password.isNotBlank()
    }

    fun signIn() {
        viewModelScope.launch {
            _apiState.value = ApiState.LOADING
            _isButtonEnable.value = false
            val response = authRepository.signIn(
                userName = email,
                password = password,
                options = AuthSignInOptions.defaults()
            )

            if (response.status == Status.SUCCESS) {
                getUserInfo()
            } else {
                _apiState.value = ApiState.Empty
                _appError.value = response.appError
            }

            validation()
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
}