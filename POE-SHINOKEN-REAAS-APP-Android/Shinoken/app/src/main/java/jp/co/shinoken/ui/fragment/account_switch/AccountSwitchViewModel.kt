package jp.co.shinoken.ui.fragment.account_switch

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.api.UserInfo
import jp.co.shinoken.repository.AuthRepository
import jp.co.shinoken.repository.StoreRepository
import jp.co.shinoken.repository.UserInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AccountSwitchViewModel @ViewModelInject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val storeRepository: StoreRepository,
    private val authRepository: AuthRepository
) :
    ViewModel() {

    val userInfo: StateFlow<UserInfo?> get() = _userInfo
    private val _userInfo = MutableStateFlow<UserInfo?>(null)

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    val apiState: StateFlow<ApiState> get() = _apiState
    private val _apiState = MutableStateFlow(ApiState.Empty)

    val isNotExistsUser: StateFlow<Boolean> get() = _isNotExistsUser
    private val _isNotExistsUser = MutableStateFlow(false)

    init {
        fetch()
    }

    private fun fetch() {
        _apiState.value = ApiState.LOADING
        viewModelScope.launch {
            val cognitoToken = authRepository.getCognitoToken().data ?: return@launch
            val response = userInfoRepository.getUserInfo(cognitoToken)
            if (response.status == Status.SUCCESS) {
                val userInfo = response.data!!
                _userInfo.value = userInfo

                if (userInfo.data.detectedAccountables.isNotEmpty()) {
                    _apiState.value = ApiState.Empty
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

    fun accountSwitch(authId: String) {
        viewModelScope.launch {
            _apiState.value = ApiState.LOADING
            val cognitoTokenResponse = authRepository.getCognitoToken()
            if (cognitoTokenResponse.status != Status.SUCCESS) {
                _appError.value = AppError.UserIdNotExistsError
                return@launch
            }

            if (storeRepository.getUUID().isNullOrEmpty()) {
                storeRepository.saveUUID()
            }
            val response = userInfoRepository.authorizeExecute(
                authorization = cognitoTokenResponse.data!!,
                authId = authId,
                deviceId = storeRepository.getUUID()!!
            )

            if (response.status == Status.SUCCESS) {
                val responseData = response.data!!.data
                storeRepository.saveApiToken(responseData.accessToken)
                storeRepository.saveCurrentAccountId(authId)
                responseData.refreshToken?.let {
                    storeRepository.saveRefreshToken(it)
                }
                _apiState.value = ApiState.Success
            } else {
                _apiState.value = ApiState.Empty
            }
        }
    }

    fun isCurrentAccount(authId: String): Boolean {
        val currentAuthId = storeRepository.getCurrentAccountId()
        return authId == currentAuthId
    }
}