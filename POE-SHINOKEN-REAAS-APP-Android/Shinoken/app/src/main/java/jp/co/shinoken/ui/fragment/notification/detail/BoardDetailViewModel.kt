package jp.co.shinoken.ui.fragment.notification.detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.api.Board
import jp.co.shinoken.model.api.Notification
import jp.co.shinoken.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BoardDetailViewModel @ViewModelInject constructor(
    private val notificationRepository: NotificationRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val boardDetail: StateFlow<Board?> get() = _boardDetail
    private val _boardDetail = MutableStateFlow<Board?>(null)

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    val isLoading: StateFlow<Boolean> get() = _isLoading
    private val _isLoading = MutableStateFlow(false)

    init {
        fetch()
    }

    fun fetch() {
        _appError.value = null
        val boardId = savedStateHandle.get<Int>("board_id")
        if (boardId == null) {
            _appError.value = AppError.ArgumentNotException
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            val response = notificationRepository.getBoardDetail(boardId)
            if (response.status == Status.SUCCESS) {
                _boardDetail.value = response.data?.data ?: return@launch
            } else {
                _appError.value = response.appError
            }
            _isLoading.value = false
        }
    }
}
