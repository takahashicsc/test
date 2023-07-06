package jp.co.shinoken.repository

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.request.RequestInquiryForm
import jp.co.shinoken.model.api.Inquiries
import jp.co.shinoken.model.api.ResponseSuccessInquiry

interface ContactRepository {
    suspend fun getInquiries(): ApiResponse<Inquiries>

    suspend fun postInquiry(requestInquiryForm: RequestInquiryForm): ApiResponse<ResponseSuccessInquiry>
}