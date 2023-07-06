package jp.co.shinoken.repository

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.model.api.BillDetail
import jp.co.shinoken.model.api.Bills

interface ChargeRepository {
    suspend fun getCharges(): ApiResponse<Bills>
    suspend fun getChargeDetail(month: String): ApiResponse<BillDetail>
}