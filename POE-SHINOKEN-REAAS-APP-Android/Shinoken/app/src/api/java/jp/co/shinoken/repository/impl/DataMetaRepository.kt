package jp.co.shinoken.repository.impl

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.ResponseHandler
import jp.co.shinoken.api.ShinokenBootApiService
import jp.co.shinoken.model.api.Me
import jp.co.shinoken.repository.MetaRepository
import javax.inject.Inject

class DataMetaRepository @Inject constructor(private val shinokenBootApiService: ShinokenBootApiService) :
    MetaRepository {
    private val responseHandler = ResponseHandler()

    override suspend fun getMeta(): ApiResponse<Me.MetaParent> {
        return try {
            responseHandler.handleSuccess(
                shinokenBootApiService.getMeta()
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}