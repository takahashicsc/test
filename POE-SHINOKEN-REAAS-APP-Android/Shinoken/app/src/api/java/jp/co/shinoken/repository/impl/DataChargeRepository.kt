package jp.co.shinoken.repository.impl

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.ResponseHandler
import jp.co.shinoken.api.ShinokenApiService
import jp.co.shinoken.model.api.BillDetail
import jp.co.shinoken.model.api.Bills
import jp.co.shinoken.repository.ChargeRepository
import javax.inject.Inject

class DataChargeRepository @Inject constructor(private val shinokenApiService: ShinokenApiService) :
    ChargeRepository {
    private val responseHandler = ResponseHandler()

    override suspend fun getCharges(): ApiResponse<Bills> {
        return try {
            responseHandler.handleSuccess(shinokenApiService.getBills())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun getChargeDetail(month: String): ApiResponse<BillDetail> {
        return try {
            responseHandler.handleSuccess(shinokenApiService.getBillDetail(month))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}