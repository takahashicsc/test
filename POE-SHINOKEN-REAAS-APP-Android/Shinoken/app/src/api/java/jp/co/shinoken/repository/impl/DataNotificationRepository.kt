package jp.co.shinoken.repository.impl

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.ResponseHandler
import jp.co.shinoken.api.ShinokenApiService
import jp.co.shinoken.model.api.BoardDetail
import jp.co.shinoken.model.api.Boards
import jp.co.shinoken.model.api.NotificationDetail
import jp.co.shinoken.model.api.Notifications
import jp.co.shinoken.repository.NotificationRepository
import javax.inject.Inject


class DataNotificationRepository @Inject constructor(private val shinokenApiService: ShinokenApiService) :
    NotificationRepository {
    private val responseHandler = ResponseHandler()

    override suspend fun getBoards(offset: Int?): ApiResponse<Boards> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.getBoards(offset)
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun getBoardDetail(id: Int): ApiResponse<BoardDetail> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.getBoardDetail(id)
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun getNotifications(offset: Int?): ApiResponse<Notifications> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.getNotifications(offset)
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun getNotificationDetail(id: Int): ApiResponse<NotificationDetail> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.getNotificationDetail(id)
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun readNotification(id: Int): ApiResponse<NotificationDetail> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.readNotification(id)
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

}