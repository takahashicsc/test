package jp.co.shinoken.ui.fragment.faq

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.FaqCategory
import jp.co.shinoken.model.FaqContent
import jp.co.shinoken.model.Faqs
import jp.co.shinoken.model.api.Suggestion
import jp.co.shinoken.repository.FaqRepository
import jp.co.shinoken.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FaqsViewModel @ViewModelInject constructor(
    private val faqRepository: FaqRepository,
    private val homeRepository: HomeRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
) :
    ViewModel() {

    val faqs: StateFlow<Faqs?> get() = _faqs
    private val _faqs = MutableStateFlow<Faqs?>(null)

    val selectFaqContents: StateFlow<List<FaqContent>> get() = _selectFaqContents
    private val _selectFaqContents = MutableStateFlow<List<FaqContent>>(listOf())

    val suggestions: StateFlow<List<Suggestion>> get() = _suggestions
    private val _suggestions = MutableStateFlow<List<Suggestion>>(listOf())

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    val isLoading: StateFlow<Boolean> get() = _isLoading
    private val _isLoading = MutableStateFlow(false)

    init {
        fetch()
        getHomeData()
    }

    fun setSelectCategory(faqCategory: FaqCategory) {
        val faqs = faqs.value ?: return
        _selectFaqContents.value = faqs.data.filter { it.categories.contains(faqCategory) }

        _faqs.value = Faqs(
            categories = faqs.categories.map {
                FaqCategory(
                    it.id,
                    it.name,
                    it.path,
                    it.image,
                    it.status,
                    it.serialCode,
                    it == faqCategory
                )
            },
            total = faqs.total,
            data = faqs.data
        )
    }

    fun fetch() {
        _isLoading.value = true
        _appError.value = null
        viewModelScope.launch {
            val response = faqRepository.getFaqs()
            if (response.status == Status.SUCCESS) {
                _faqs.value = response.data!!
                val categoryName = savedStateHandle.get<String?>("category_name")
                setSelectCategory(faqs.value!!.categories.firstOrNull { it.name == categoryName }
                    ?: faqs.value!!.categories.first())
            } else {
                _appError.value = response.appError!!
            }
            _isLoading.value = false
        }
    }

    private fun getHomeData() {
        _appError.value = null
        viewModelScope.launch {
            val response = homeRepository.getHome()

            if (response.status == Status.SUCCESS) {
                _suggestions.value = response.data!!.suggestions
            } else {
                _appError.value = response.appError
            }
        }
    }
}