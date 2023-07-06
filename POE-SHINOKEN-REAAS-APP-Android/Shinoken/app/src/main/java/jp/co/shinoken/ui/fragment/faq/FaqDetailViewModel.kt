package jp.co.shinoken.ui.fragment.faq

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.FaqContent
import jp.co.shinoken.repository.FaqRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FaqDetailViewModel @ViewModelInject constructor(
    private val faqRepository: FaqRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val faqDetail: StateFlow<FaqContent?> get() = _faqDetail
    private val _faqDetail = MutableStateFlow<FaqContent?>(null)

    val isLoading: StateFlow<Boolean> get() = _isLoading
    private val _isLoading = MutableStateFlow(false)

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    init {
        fetch()
    }

    fun fetch() {
        val faqId = savedStateHandle.get<Int>("faq_id")
        _appError.value = null
        if (faqId == null) {
            _appError.value = AppError.ArgumentNotException
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            val response = faqRepository.getFaqDetail(faqId)
            if (response.status == Status.SUCCESS) {
                _faqDetail.value = response.data?.data ?: return@launch
            } else {
                _appError.value = response.appError
            }
            _isLoading.value = false
        }
    }
}