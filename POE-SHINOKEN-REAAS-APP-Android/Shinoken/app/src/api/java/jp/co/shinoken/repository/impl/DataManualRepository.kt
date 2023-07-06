package jp.co.shinoken.repository.impl

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.ResponseHandler
import jp.co.shinoken.api.ShinokenApiService
import jp.co.shinoken.model.api.Manuals
import jp.co.shinoken.repository.ManualRepository
import javax.inject.Inject

class DataManualRepository @Inject constructor(private val shinokenApiService: ShinokenApiService) :
    ManualRepository {
    private val responseHandler = ResponseHandler()
    override suspend fun getManuals(): ApiResponse<Manuals> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.getManuals()
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}