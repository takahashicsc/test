package jp.co.shinoken.ui.fragment.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.HomeMessage
import jp.co.shinoken.model.api.Home
import jp.co.shinoken.model.api.PushToken
import jp.co.shinoken.repository.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime

class HomeViewModel @ViewModelInject constructor(
    private val homeRepository: HomeRepository,
    private val userInfoRepository: UserInfoRepository,
    private val authRepository: AuthRepository,
    private val storeRepository: StoreRepository,
    private val pushRepository: PushRepository,
) :
    ViewModel() {

    val homeData: StateFlow<Home?>
        get() = _homeData
    private val _homeData = MutableStateFlow<Home?>(null)

    val messageRes: StateFlow<Int?>
        get() = _messageRes
    private val _messageRes = MutableStateFlow<Int?>(null)

    val isSwitchAccount: StateFlow<Boolean>
        get() = _isSwitchAccount
    private val _isSwitchAccount = MutableStateFlow<Boolean>(false)

    val isLoading: StateFlow<Boolean> get() = _isLoading
    private val _isLoading = MutableStateFlow(false)

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    init {
        fetch()
        postPushToken()
    }

    fun fetch() {
        _isLoading.value = true
        _appError.value = null
        getHomeMessage()
        viewModelScope.launch {
            val cognitoTokenResponse = authRepository.getCognitoToken()
            if (cognitoTokenResponse.status == Status.SUCCESS) {
                val userInfoResponse = userInfoRepository.getUserInfo(cognitoTokenResponse.data!!)
                if (userInfoResponse.status == Status.SUCCESS) {
                    val size = userInfoResponse.data?.data?.detectedAccountables?.size
                    _isSwitchAccount.value = size != null && size > 1
                } else {
                    _appError.value = userInfoResponse.appError
                }
            } else {
                _appError.value = cognitoTokenResponse.appError
            }


            val response = homeRepository.getHome()
            if (response.status == Status.SUCCESS) {
                val homeData = response.data!!
                _homeData.value = homeData
            } else {
                _appError.value = response.appError
            }

            _isLoading.value = false
        }
    }

    private fun getHomeMessage() {
        val currentTime = LocalTime.now()
        _messageRes.value = HomeMessage(currentTime).homeMessageRes
    }

    private fun postPushToken() {
        viewModelScope.launch {
            val pushToken = storeRepository.getPushToken() ?: return@launch
            if (storeRepository.getUUID().isNullOrEmpty()) {
                storeRepository.saveUUID()
            }
            if (storeRepository.isPostPushToken().not() && pushToken.isBlank().not()) {
                val response = pushRepository.postToken(
                    PushToken(
                        deviceId = storeRepository.getUUID()!!,
                        token = pushToken,
                    )
                )
                if (response.status == Status.SUCCESS) {
                    storeRepository.savePushToken(pushToken = pushToken, isPost = true)
                }
            }
        }
    }
}