package jp.co.shinoken.repository

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.model.api.TokenRefresh
import jp.co.shinoken.model.api.UserInfo

interface UserInfoRepository {
    suspend fun getUserInfo(authorization: String): ApiResponse<UserInfo>

    suspend fun authorizeExecute(
        authorization: String,
        authId: String,
        deviceId: String
    ): ApiResponse<TokenRefresh>

    suspend fun tokenRefresh(refreshToken: String): ApiResponse<TokenRefresh>
}