package jp.co.shinoken.repository.impl

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.ResponseHandler
import jp.co.shinoken.api.ShinokenApiService
import jp.co.shinoken.model.api.PushToken
import jp.co.shinoken.repository.PushRepository
import javax.inject.Inject

class DataPushRepository @Inject constructor(private val shinokenApiService: ShinokenApiService) :
    PushRepository {
    private val responseHandler = ResponseHandler()
    override suspend fun postToken(pushToken: PushToken): ApiResponse<Any> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.postPushToken(pushToken)
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}