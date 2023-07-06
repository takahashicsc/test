package jp.co.shinoken.ui.fragment.cohabitant

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.CohabitantParent
import jp.co.shinoken.repository.RoommateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CohabitantsViewModel @ViewModelInject constructor(private val roommateRepository: RoommateRepository) :
    ViewModel() {

    val cohabitants: StateFlow<List<CohabitantParent>> get() = _cohabitants
    private val _cohabitants = MutableStateFlow<List<CohabitantParent>>(listOf())

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    val isLoading: StateFlow<Boolean> get() = _isLoading
    private val _isLoading = MutableStateFlow(false)

    fun fetch() {
        _isLoading.value = true
        _appError.value = null
        viewModelScope.launch {
            val response = roommateRepository.getReside()
            if (response.status == Status.SUCCESS) {
                _cohabitants.value = CohabitantParent.convertApiResponse(response.data!!.data)
            } else {
                _appError.value = response.appError
            }
            _isLoading.value = false
        }
    }
}