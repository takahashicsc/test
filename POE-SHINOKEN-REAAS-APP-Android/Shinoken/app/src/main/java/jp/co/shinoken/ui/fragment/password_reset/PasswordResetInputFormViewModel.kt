package jp.co.shinoken.ui.fragment.password_reset

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.AlertError
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.model.AppError
import jp.co.shinoken.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PasswordResetInputFormViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    @Assisted savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    val isButtonEnable: StateFlow<Boolean> get() = _isButtonEnable
    private val _isButtonEnable = MutableStateFlow(false)

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    val apiState: StateFlow<ApiState> get() = _apiState

    private val _apiState = MutableStateFlow(ApiState.Empty)
    var code: String = ""
    var password: String = ""
    var passwordConfirm: String = ""

    val userName: String = savedStateHandle.get<String>("userName") ?: ""

    private fun onAppError() {
        _appError.value = AppError.ArgumentNotException
    }

    fun validation() {
        _isButtonEnable.value =
            code.isNotBlank() && password.isNotBlank() && passwordConfirm.isNotBlank()
    }

    fun confirmResetPassword() {
        if (password != passwordConfirm) {
            _appError.value =
                AppError.ApiException.AlertAppError(
                    cause = null,
                    alertError = AlertError("", "新しいパスワードと新しいパスワードをもう一度入力が正しくありません")
                )
            return
        }
        _apiState.value = ApiState.LOADING
        _isButtonEnable.value = false
        viewModelScope.launch {
            val response = authRepository.confirmResetPassword(
                newPassword = password,
                confirmationCode = code
            )

            if (response.status == Status.SUCCESS) {
                _apiState.value = ApiState.Success
            } else {
                _apiState.value = ApiState.Empty
                _appError.value = response.appError
            }

            validation()
        }
    }

    fun resendCode() {
        if (userName.isBlank()) {
            onAppError()
            return
        }
        _apiState.value = ApiState.LOADING
        viewModelScope.launch {
            val response = authRepository.resetPassword(
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