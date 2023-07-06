package jp.co.shinoken.repository

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.model.api.PushToken

interface PushRepository {
    suspend fun postToken(pushToken: PushToken): ApiResponse<Any>
}