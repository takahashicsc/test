package jp.co.shinoken.repository

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.model.api.Me

interface MetaRepository {
    suspend fun getMeta(): ApiResponse<Me.MetaParent>
}