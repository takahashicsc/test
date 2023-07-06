package jp.co.shinoken.repository

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.model.api.BoardDetail
import jp.co.shinoken.model.api.Boards
import jp.co.shinoken.model.api.NotificationDetail
import jp.co.shinoken.model.api.Notifications

interface NotificationRepository {
    suspend fun getBoards(offset: Int? = null): ApiResponse<Boards>
    suspend fun getBoardDetail(id: Int): ApiResponse<BoardDetail>

    suspend fun getNotifications(offset: Int? = null): ApiResponse<Notifications>
    suspend fun getNotificationDetail(id: Int): ApiResponse<NotificationDetail>

    suspend fun readNotification(id: Int): ApiResponse<NotificationDetail>
}