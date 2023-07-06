package jp.co.shinoken.repository.impl

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.ResponseHandler
import jp.co.shinoken.api.ShinokenApiService
import jp.co.shinoken.model.api.Benefits
import jp.co.shinoken.repository.BenefitRepository
import javax.inject.Inject

class DataBenefitRepository @Inject constructor(private val shinokenApiService: ShinokenApiService) :
    BenefitRepository {
    private val responseHandler = ResponseHandler()
    override suspend fun getBenefits(offset: Int?): ApiResponse<Benefits> {
        return try {
            responseHandler.handleSuccess(shinokenApiService.getBenefits(offset = offset))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}