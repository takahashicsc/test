package jp.co.shinoken.ui.fragment.charges

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.ChargeDetail
import jp.co.shinoken.model.Charges
import jp.co.shinoken.model.ChargesLatest
import jp.co.shinoken.repository.ChargeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChargesViewModel @ViewModelInject constructor(
    private val chargeRepository: ChargeRepository,
) : ViewModel() {

    val charges: StateFlow<Charges> get() = _charges
    private val _charges = MutableStateFlow(Charges(listOf(), null))

    val latestCharge: StateFlow<ChargesLatest?> get() = _latestCharge
    private val _latestCharge = MutableStateFlow<ChargesLatest?>(null)

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    val isLoading: StateFlow<Boolean> get() = _isLoading
    private val _isLoading = MutableStateFlow(false)

    init {
        fetch()
    }

    fun fetch() {
        _isLoading.value = true
        _appError.value = null
        viewModelScope.launch {
            val response = chargeRepository.getCharges()
            if (response.status == Status.SUCCESS) {
                _charges.value = Charges.convertApiResponse(response.data!!)
                response.data.latest?.let {
                    _latestCharge.value = ChargesLatest.convertApiResponse(response.data.latest)
                }
            } else {
                _appError.value = response.appError
            }
            _isLoading.value = false
        }
    }
}