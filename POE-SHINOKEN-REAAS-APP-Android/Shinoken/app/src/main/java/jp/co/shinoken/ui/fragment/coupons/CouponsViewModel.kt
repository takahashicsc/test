package jp.co.shinoken.ui.fragment.coupons

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.CheckCategory
import jp.co.shinoken.model.api.Benefit
import jp.co.shinoken.model.api.Benefits
import jp.co.shinoken.model.api.Board
import jp.co.shinoken.repository.BenefitRepository
import jp.co.shinoken.ui.fragment.check_form.CheckFormFragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CouponsViewModel @ViewModelInject constructor(private val benefitRepository: BenefitRepository) :
    ViewModel() {

    val benefits: StateFlow<List<Benefit>>
        get() = _benefits

    private val _benefits = MutableStateFlow<List<Benefit>>(listOf())

    private val totalCount = MutableStateFlow<Int?>(null)

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    val isLoading: StateFlow<Boolean> get() = _isLoading
    private val _isLoading = MutableStateFlow(false)

    val isNextLoading: StateFlow<Boolean> get() = _isNextLoading
    private val _isNextLoading = MutableStateFlow(false)

    init {
        fetch()
    }

    fun fetch() {
        _isLoading.value = true
        _appError.value = null
        viewModelScope.launch {
            val response = benefitRepository.getBenefits()
            if (response.status == Status.SUCCESS) {
                _benefits.value = response.data!!.data
                totalCount.value = response.data.total
            } else {
                _appError.value = response.appError
            }
            _isLoading.value = false
        }
    }

    fun nextPageLoad() {
        if (benefits.value.size == totalCount.value) return
        _isNextLoading.value = true
        viewModelScope.launch {
            val response =
                benefitRepository.getBenefits(offset = benefits.value.size)
            if (response.status == Status.SUCCESS) {
                _benefits.value = _benefits.value + response.data!!.data
            } else {
                _appError.value = response.appError
            }
            _isNextLoading.value = false
        }
    }
}