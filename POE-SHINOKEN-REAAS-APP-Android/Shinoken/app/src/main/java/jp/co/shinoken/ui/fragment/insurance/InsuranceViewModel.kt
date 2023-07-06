package jp.co.shinoken.ui.fragment.insurance

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.api.Me
import jp.co.shinoken.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InsuranceViewModel @ViewModelInject constructor(private val homeRepository: HomeRepository) :
    ViewModel() {
    val insurance: StateFlow<Me.Insurance?> get() = _insurance
    private val _insurance = MutableStateFlow<Me.Insurance?>(null)

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
            val response = homeRepository.getMe()
            if (response.status == Status.SUCCESS) {
                _insurance.value = response.data!!.data.insurance
            } else {
                _appError.value = response.appError
            }
            _isLoading.value = false
        }
    }
}