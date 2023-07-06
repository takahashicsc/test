package jp.co.shinoken.ui.fragment.charges

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.ChargeDetail
import jp.co.shinoken.repository.ChargeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChargeDetailViewModel @ViewModelInject constructor(
    private val repository: ChargeRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val chargeDetail: StateFlow<ChargeDetail?> get() = _chargeDetail
    private val _chargeDetail = MutableStateFlow<ChargeDetail?>(null)

    val isLoading: StateFlow<Boolean> get() = _isLoading
    private val _isLoading = MutableStateFlow(false)

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    init {
        fetch()
    }

    fun fetch() {
        val month = savedStateHandle.get<String>("charge_month")
        _appError.value = null
        if (month == null) {
            _appError.value = AppError.ArgumentNotException
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            val response = repository.getChargeDetail(month)
            if (response.status == Status.SUCCESS) {
                _chargeDetail.value = ChargeDetail.convertApiResponse(response.data!!.data)
            } else {
                _appError.value = response.appError
            }
            _isLoading.value = false
        }
    }
}