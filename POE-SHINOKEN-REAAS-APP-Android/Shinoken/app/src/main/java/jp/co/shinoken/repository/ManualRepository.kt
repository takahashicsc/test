package jp.co.shinoken.repository

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.model.api.Manuals

interface ManualRepository {
    suspend fun getManuals(): ApiResponse<Manuals>
}