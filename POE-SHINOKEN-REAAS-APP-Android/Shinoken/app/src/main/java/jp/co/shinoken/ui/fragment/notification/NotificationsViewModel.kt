package jp.co.shinoken.ui.fragment.notification

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.api.Notification
import jp.co.shinoken.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationsViewModel @ViewModelInject constructor(private val notificationRepository: NotificationRepository) :
    ViewModel() {

    val unReadCount: StateFlow<Int> get() = _unReadCount
    private val _unReadCount = MutableStateFlow(0)

    val notifications: StateFlow<List<Notification>> get() = _notifications
    private val _notifications = MutableStateFlow<List<Notification>>(listOf())

    private val totalCount = MutableStateFlow<Int?>(null)

    val isNextLoading: StateFlow<Boolean> get() = _isNextLoading
    private val _isNextLoading = MutableStateFlow(false)

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
            val response = notificationRepository.getNotifications()
            if (response.status == Status.SUCCESS) {
                _notifications.value = response.data!!.data
                _unReadCount.value = response.data.unreadCount
                totalCount.value = response.data.total
            } else {
                _appError.value = response.appError
            }
            _isLoading.value = false
        }
    }

    fun nextPageLoad() {
        if (notifications.value.size == totalCount.value) return
        _isNextLoading.value = true
        viewModelScope.launch {
            val response =
                notificationRepository.getNotifications(offset = notifications.value.size)
            if (response.status == Status.SUCCESS) {
                _notifications.value = _notifications.value + response.data!!.data
                _unReadCount.value = response.data.unreadCount
            } else {
                _appError.value = response.appError
            }
            _isNextLoading.value = false
        }
    }
}