package jp.co.shinoken.repository

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.model.FaqDetail
import jp.co.shinoken.model.Faqs

interface FaqRepository {
    suspend fun getFaqs(): ApiResponse<Faqs>
    suspend fun getFaqDetail(id: Int): ApiResponse<FaqDetail>
}