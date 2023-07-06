package jp.co.shinoken.repository.impl

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.ResponseHandler
import jp.co.shinoken.api.ShinokenApiService
import jp.co.shinoken.model.FaqDetail
import jp.co.shinoken.model.Faqs
import jp.co.shinoken.repository.FaqRepository
import javax.inject.Inject

class DataFaqRepository @Inject constructor(private val shinokenApiService: ShinokenApiService) :
    FaqRepository {
    private val responseHandler = ResponseHandler()
    override suspend fun getFaqs(): ApiResponse<Faqs> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.getFaqs()
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


    override suspend fun getFaqDetail(id: Int): ApiResponse<FaqDetail> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.getFaqs(id)
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }

    }
}