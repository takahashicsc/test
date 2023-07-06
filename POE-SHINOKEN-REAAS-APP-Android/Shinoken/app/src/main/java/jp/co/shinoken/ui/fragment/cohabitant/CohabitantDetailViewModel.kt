package jp.co.shinoken.ui.fragment.cohabitant

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.Cohabitant
import jp.co.shinoken.model.ResideType
import jp.co.shinoken.model.api.Me
import jp.co.shinoken.repository.HomeRepository
import jp.co.shinoken.repository.RoommateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CohabitantDetailViewModel @ViewModelInject constructor(
    private val roommateRepository: RoommateRepository,
    private val userRepository: HomeRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val cohabitant: StateFlow<Cohabitant?> get() = _cohabitant
    private val _cohabitant = MutableStateFlow<Cohabitant?>(null)

    val resideType: StateFlow<ResideType?> get() = _resideType
    private val _resideType = MutableStateFlow<ResideType?>(null)

    val isShowChargeButton: StateFlow<Boolean> get() = _isShowChargeButton
    private val _isShowChargeButton = MutableStateFlow<Boolean>(false)

    val isShowCharge: StateFlow<Boolean>
        get() = _isShowCharge
    private val _isShowCharge = MutableStateFlow(false)

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    val deleteApiError: StateFlow<AppError?>
        get() = _deleteApiError
    private val _deleteApiError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    val apiState: StateFlow<ApiState> get() = _apiState
    private val _apiState = MutableStateFlow(ApiState.Empty)

    init {
        fetch()
    }

    fun fetch() {
        _appError.value = null
        val resideType = savedStateHandle.get<ResideType>("resideType")
        val cohabitant = savedStateHandle.get<Cohabitant>("cohabitant")
        if (resideType == null || cohabitant == null) {
            _appError.value = AppError.ArgumentNotException
            return
        }
        if (resideType != ResideType.Cohabitant) {
            getAccountableType(resideType)
            getSettings()
        }
        _resideType.value = resideType
        _cohabitant.value = cohabitant
    }

    private fun getAccountableType(resideType: ResideType) {
        _deleteApiError.value = null
        viewModelScope.launch {
            val response =
                userRepository.getMe()

            if (response.status == Status.SUCCESS) {
                _isShowChargeButton.value =
                    response.data!!.data.accountableType == Me.AccountableType.Resident && resideType != ResideType.Cohabitant
            }
        }
    }

    fun deleteCohabitant() {
        _deleteApiError.value = null
        viewModelScope.launch {
            _apiState.value = ApiState.LOADING
            val code = _cohabitant.value?.code ?: return@launch
            val response =
                roommateRepository.deleteRoomMateRequest(code)

            if (response.status == Status.SUCCESS) {
                _apiState.value = ApiState.Success
            } else {
                _apiState.value = ApiState.Empty
                _deleteApiError.value = response.appError
            }
        }
    }

    fun getSettings() {
        _appError.value = null
        viewModelScope.launch {
            val response = roommateRepository.getSetting()
            if (response.status == Status.SUCCESS) {
                _isShowCharge.value = response.data!!.data.allowedSubresidentToCheckBilling
            } else {
                _appError.value = response.appError
            }
        }
    }

    fun patchSubresidentBillingActive() {
        _appError.value = null
        viewModelScope.launch {
            val response = roommateRepository.patchSubresidentBillingActive()
            if (response.status == Status.SUCCESS) {
                _isShowCharge.value = response.data!!.data.allowedSubresidentToCheckBilling
            } else {
                _appError.value = response.appError
            }
        }
    }

    fun patchSubresidentBillingInActive() {
        _appError.value = null
        viewModelScope.launch {
            val response = roommateRepository.patchSubresidentBillingInActive()
            if (response.status == Status.SUCCESS) {
                _isShowCharge.value = response.data!!.data.allowedSubresidentToCheckBilling
            } else {
                _appError.value = response.appError
            }
        }
    }
}