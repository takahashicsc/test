package jp.co.shinoken.repository

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.model.api.Benefits

interface BenefitRepository {
    suspend fun getBenefits(offset: Int? = null): ApiResponse<Benefits>
}