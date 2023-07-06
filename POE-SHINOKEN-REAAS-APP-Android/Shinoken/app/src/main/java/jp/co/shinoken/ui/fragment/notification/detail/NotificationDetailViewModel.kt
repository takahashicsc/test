package jp.co.shinoken.ui.fragment.notification.detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.api.Notification
import jp.co.shinoken.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationDetailViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    val notificationDetail: StateFlow<Notification?> get() = _notificationDetail
    private val _notificationDetail = MutableStateFlow<Notification?>(null)

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
        val notificationId = savedStateHandle.get<Int>("notification_id")
        if (notificationId == null) {
            _appError.value = AppError.ArgumentNotException
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            val response = notificationRepository.getNotificationDetail(notificationId)
            if (response.status == Status.SUCCESS) {
                val notificationDetail = response.data!!.data
                _notificationDetail.value = notificationDetail
                notificationRepository.readNotification(notificationDetail.id)
            } else {
                _appError.value = response.appError
            }
            _isLoading.value = false
        }
    }
}