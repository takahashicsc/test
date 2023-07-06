package jp.co.shinoken.repository

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.request.RequestRoomMate
import jp.co.shinoken.model.api.*

interface RoommateRepository {
    suspend fun getReside(): ApiResponse<ResideParent>
    suspend fun getRoommateDetail(id: Int): ApiResponse<RoommateDetail>
    suspend fun postRoommate(requestRoomMate: RequestRoomMate): ApiResponse<ResponseSuccessRoommate>
    suspend fun deleteRoomMateRequest(code: String): ApiResponse<ResponseSuccessRoommate>
    suspend fun getSetting(): ApiResponse<Settings>
    suspend fun patchSubresidentBillingActive(): ApiResponse<ResponseSettings>
    suspend fun patchSubresidentBillingInActive(): ApiResponse<ResponseSettings>
}