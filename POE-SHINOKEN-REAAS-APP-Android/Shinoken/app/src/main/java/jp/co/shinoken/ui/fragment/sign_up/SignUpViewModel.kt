package jp.co.shinoken.ui.fragment.sign_up

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import jp.co.shinoken.api.Status
import jp.co.shinoken.extension.toInternationalPhoneNumber
import jp.co.shinoken.model.AlertError
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.model.AppError
import jp.co.shinoken.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel @ViewModelInject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    val isButtonEnable: StateFlow<Boolean> get() = _isButtonEnable
    private val _isButtonEnable = MutableStateFlow(false)

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    val apiState: StateFlow<ApiState> get() = _apiState
    private val _apiState = MutableStateFlow(ApiState.Empty)


    var name: String = ""
    var mail: String = ""
    var phoneNumber: String = ""
    var password: String = ""
    var passwordConfirm: String = ""

    fun setApiState(state: ApiState) {
        _apiState.value = state
    }

    fun validation() {
        _isButtonEnable.value =
            name.isNotBlank() && mail.isNotBlank() && phoneNumber.isNotBlank() && password.isNotEmpty() && passwordConfirm.isNotEmpty()
    }

    fun signUp() {
        if (password != passwordConfirm) {
            _appError.value =
                AppError.ApiException.AlertAppError(
                    cause = null,
                    alertError = AlertError("", "パスワードとパスワード再入力の値が正しくありません")
                )
            return
        }
        viewModelScope.launch {
            _apiState.value = ApiState.LOADING
            _isButtonEnable.value = false
            val signUpResponse = authRepository.signUp(
                userName = mail,
                password = password,
                options = AuthSignUpOptions.builder()
                    .userAttributes(
                        listOf(
                            AuthUserAttribute(AuthUserAttributeKey.name(), name),
                            AuthUserAttribute(AuthUserAttributeKey.email(), mail),
                            AuthUserAttribute(
                                AuthUserAttributeKey.phoneNumber(),
                                phoneNumber.toInternationalPhoneNumber
                            )
                        )
                    )
                    .build(),
            )

            if (signUpResponse.status == Status.SUCCESS) {
                _apiState.value = ApiState.Success

            } else {
                _apiState.value = ApiState.Empty
                _appError.value = signUpResponse.appError
            }

            validation()
        }
    }
}