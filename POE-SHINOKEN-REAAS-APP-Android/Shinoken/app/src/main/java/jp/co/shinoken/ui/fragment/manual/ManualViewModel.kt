package jp.co.shinoken.ui.fragment.manual

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.api.Manuals
import jp.co.shinoken.repository.ManualRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ManualViewModel @ViewModelInject constructor(private val manualRepository: ManualRepository) :
    ViewModel() {

    val manuals: StateFlow<Manuals?> get() = _manuals
    private val _manuals = MutableStateFlow<Manuals?>(null)

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
            val response = manualRepository.getManuals()
            if (response.status == Status.SUCCESS) {
                _manuals.value = response.data!!
            } else {
                _appError.value = response.appError
            }
            _isLoading.value = false
        }
    }
}