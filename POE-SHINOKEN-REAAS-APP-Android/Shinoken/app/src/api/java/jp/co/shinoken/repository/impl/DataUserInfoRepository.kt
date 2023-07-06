package jp.co.shinoken.repository.impl

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.ResponseHandler
import jp.co.shinoken.api.ShinokenBootApiService
import jp.co.shinoken.model.api.TokenRefresh
import jp.co.shinoken.model.api.UserInfo
import jp.co.shinoken.repository.UserInfoRepository
import javax.inject.Inject

class DataUserInfoRepository @Inject constructor(private val shinokenBootApiService: ShinokenBootApiService) :
    UserInfoRepository {

    private val responseHandler = ResponseHandler()
    override suspend fun getUserInfo(authorization: String): ApiResponse<UserInfo> {
        return try {
            responseHandler.handleSuccess(
                shinokenBootApiService.getUserInfo("Bearer $authorization")
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun authorizeExecute(
        authorization: String,
        authId: String,
        deviceId: String
    ): ApiResponse<TokenRefresh> {
        return try {
            responseHandler.handleSuccess(
                shinokenBootApiService.authorizeExecute(
                    authorization = "Bearer $authorization",
                    authId = authId,
                    deviceId = deviceId
                )
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun tokenRefresh(refreshToken: String): ApiResponse<TokenRefresh> {
        return try {
            responseHandler.handleSuccess(
                shinokenBootApiService.tokenRefresh("Bearer $refreshToken")
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}