package jp.co.shinoken.ui.fragment.notification.boards

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.api.Board
import jp.co.shinoken.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BoardsItemViewModel
@ViewModelInject constructor(private val notificationRepository: NotificationRepository) :
    ViewModel() {
    val boards: StateFlow<List<Board>> get() = _boards
    private val _boards = MutableStateFlow<List<Board>>(listOf())

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
            val response = notificationRepository.getBoards()
            if (response.status == Status.SUCCESS) {
                _boards.value = response.data!!.data
                totalCount.value = response.data.total
            } else {
                _appError.value = response.appError
            }
            _isLoading.value = false
        }
    }

    fun nextPageLoad() {
        if (boards.value.size == totalCount.value) return
        _isNextLoading.value = true
        viewModelScope.launch {
            val response =
                notificationRepository.getBoards(offset = boards.value.size)
            if (response.status == Status.SUCCESS) {
                _boards.value = _boards.value + response.data!!.data
            } else {
                _appError.value = response.appError
            }
            _isNextLoading.value = false
        }
    }
}