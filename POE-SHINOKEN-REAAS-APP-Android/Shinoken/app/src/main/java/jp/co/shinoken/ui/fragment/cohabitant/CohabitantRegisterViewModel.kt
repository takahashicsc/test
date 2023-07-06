package jp.co.shinoken.ui.fragment.cohabitant

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.api.request.RequestRoomMate
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.BirthDay
import jp.co.shinoken.repository.RoommateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CohabitantRegisterViewModel @ViewModelInject constructor(private val roommateRepository: RoommateRepository) :
    ViewModel() {

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    val apiState: StateFlow<ApiState> get() = _apiState
    private val _apiState = MutableStateFlow(ApiState.Empty)

    var email: String = ""
    var name: String = ""
    var birthDay: BirthDay = BirthDay(null, null, null)

    val isButtonEnable: StateFlow<Boolean> get() = _isButtonEnable
    private val _isButtonEnable = MutableStateFlow(false)

    fun validation() {
        _isButtonEnable.value =
            email.isNotBlank() && name.isNotBlank() && birthDay.year != null && birthDay.month != null && birthDay.day != null
    }

    fun postRoommate() {
        _apiState.value = ApiState.LOADING
        _isButtonEnable.value = false
        viewModelScope.launch {
            val response = roommateRepository.postRoommate(
                RequestRoomMate(
                    email = email,
                    name = name,
                    birthday = birthDay.birthDayHyphenString!!,
                )
            )
            if (response.status == Status.SUCCESS) {
                _apiState.value = ApiState.Success
            } else {
                _apiState.value = ApiState.Empty
                _appError.value = response.appError
            }

            _isButtonEnable.value = true
        }
    }
}