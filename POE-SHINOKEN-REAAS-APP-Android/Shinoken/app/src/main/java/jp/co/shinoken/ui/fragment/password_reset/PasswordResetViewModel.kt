package jp.co.shinoken.ui.fragment.password_reset

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.model.AppError
import jp.co.shinoken.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PasswordResetViewModel @ViewModelInject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    val isButtonEnable: StateFlow<Boolean> get() = _isButtonEnable
    private val _isButtonEnable = MutableStateFlow(false)

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    val apiState: StateFlow<ApiState> get() = _apiState
    private val _apiState = MutableStateFlow(ApiState.Empty)

    var email: String = ""

    fun validation() {
        _isButtonEnable.value = email.isNotBlank()
    }

    fun resetPassword() {
        viewModelScope.launch {
            _apiState.value = ApiState.LOADING
            _isButtonEnable.value = false
            val response = authRepository.resetPassword(
                userName = email
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
}