package jp.co.shinoken.ui.fragment.sign_in_support

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.api.request.RequestSignInSupport
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.model.AppError
import jp.co.shinoken.repository.AuthRepository
import jp.co.shinoken.repository.MetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignInSupportMailFormViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    private val metaRepository: MetaRepository
) :
    ViewModel() {

    val isButtonEnable: StateFlow<Boolean> get() = _isButtonEnable
    private val _isButtonEnable = MutableStateFlow(false)

    val privacyPolicyUrl: StateFlow<String?> get() = _privacyPolicyUrl
    private val _privacyPolicyUrl = MutableStateFlow<String?>(null)

    val apiState: StateFlow<ApiState> get() = _apiState
    private val _apiState = MutableStateFlow(ApiState.Empty)

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    var name: String = ""
    var kana: String = ""
    var phoneNumber: String = ""
    var mail: String = ""
    var content: String = ""

    init {
        getMetaData()
    }

    private fun getMetaData() {
        _appError.value = null
        viewModelScope.launch {
            val response = metaRepository.getMeta()

            if (response.status == Status.SUCCESS) {
                _privacyPolicyUrl.value = response.data!!.data.privacyPolicy
            } else {
                _appError.value = response.appError
            }
        }
    }

    fun validation() {
        _isButtonEnable.value =
            name.isNotBlank() && kana.isNotBlank() && phoneNumber.isNotBlank() && mail.isNotBlank() && content.isNotBlank()
    }

    fun postSignInSupport() {
        _apiState.value = ApiState.LOADING
        _isButtonEnable.value = false
        viewModelScope.launch {
            val response = authRepository.postSignInSupport(
                requestSignInSupport = RequestSignInSupport(
                    name = name,
                    nameKana = kana,
                    phoneNumber = phoneNumber,
                    email = mail,
                    body = content
                )
            )
            if (response.status == Status.SUCCESS) {
                _apiState.value = ApiState.Success
            } else {
                _apiState.value = ApiState.Empty
                _appError.value = response.appError
            }
        }
        _isButtonEnable.value = true
    }
}