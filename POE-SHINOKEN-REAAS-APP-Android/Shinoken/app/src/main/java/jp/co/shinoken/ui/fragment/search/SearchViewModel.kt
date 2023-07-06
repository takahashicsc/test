package jp.co.shinoken.ui.fragment.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.SearchResult
import jp.co.shinoken.repository.SearchRepository
import jp.co.shinoken.ui.fragment.check_form.CheckFormFragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel @ViewModelInject constructor(private val searchRepository: SearchRepository) :
    ViewModel() {

    val searchResult: StateFlow<SearchResult>
        get() = _searchResult
    private val _searchResult = MutableStateFlow(SearchResult(listOf()))

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    fun search(searchText: String) {
        viewModelScope.launch {
            val response = searchRepository.sendSearch(searchText)
            if (response.status == Status.SUCCESS) {
                _searchResult.value = response.data ?: return@launch
            } else {
                _appError.value = response.appError
            }
        }
    }
}