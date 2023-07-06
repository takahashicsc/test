package jp.co.shinoken.repository.impl

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.ResponseHandler
import jp.co.shinoken.api.ShinokenApiService
import jp.co.shinoken.api.request.RequestRoomMate
import jp.co.shinoken.api.request.RequestRoomMateCancel
import jp.co.shinoken.model.api.*
import jp.co.shinoken.repository.RoommateRepository
import javax.inject.Inject

class DataRoommateRepository @Inject constructor(private val shinokenApiService: ShinokenApiService) :
    RoommateRepository {
    private val responseHandler = ResponseHandler()
    override suspend fun getReside(): ApiResponse<ResideParent> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.getReside()
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun getRoommateDetail(id: Int): ApiResponse<RoommateDetail> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.getRoomMateRequest(id)
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun postRoommate(requestRoomMate: RequestRoomMate): ApiResponse<ResponseSuccessRoommate> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.postRoomMateRequest(requestRoomMate)
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun deleteRoomMateRequest(code: String): ApiResponse<ResponseSuccessRoommate> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.deleteRoomMateRequest(RequestRoomMateCancel(code = code))
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun getSetting(): ApiResponse<Settings> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.getSettings()
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun patchSubresidentBillingActive(): ApiResponse<ResponseSettings> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.patchSubresidentBillingActive()
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun patchSubresidentBillingInActive(): ApiResponse<ResponseSettings> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.patchSubresidentBillingInActive()
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

}