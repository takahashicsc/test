package jp.co.shinoken.repository.impl

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.ResponseHandler
import jp.co.shinoken.api.ShinokenApiService
import jp.co.shinoken.api.request.RequestInquiryForm
import jp.co.shinoken.model.ContactCategory
import jp.co.shinoken.model.api.Inquiries
import jp.co.shinoken.model.api.ResponseSuccessInquiry
import jp.co.shinoken.repository.ContactRepository
import javax.inject.Inject

class DataContactRepository @Inject constructor(private val shinokenApiService: ShinokenApiService) :
    ContactRepository {
    private val responseHandler = ResponseHandler()
    override suspend fun getInquiries(): ApiResponse<Inquiries> {
        return try {
            responseHandler.handleSuccess(shinokenApiService.getInquiries())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun postInquiry(requestInquiryForm: RequestInquiryForm): ApiResponse<ResponseSuccessInquiry> {
        return try {
            responseHandler.handleSuccess(shinokenApiService.postInquiry(requestInquiryForm))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}