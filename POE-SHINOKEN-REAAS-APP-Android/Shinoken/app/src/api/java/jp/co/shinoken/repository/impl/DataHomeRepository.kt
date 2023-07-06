package jp.co.shinoken.repository.impl

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.ResponseHandler
import jp.co.shinoken.api.ShinokenApiService
import jp.co.shinoken.api.response.ResponseIcon
import jp.co.shinoken.extension.convertFileRequestBody
import jp.co.shinoken.model.api.*
import jp.co.shinoken.repository.HomeRepository
import java.io.File
import javax.inject.Inject

class DataHomeRepository @Inject constructor(
    private val shinokenApiService: ShinokenApiService
) : HomeRepository {
    private val responseHandler = ResponseHandler()

    override suspend fun getHome(): ApiResponse<Home> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.getHome().data
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun getMe(): ApiResponse<MeParent> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.getMe()
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


    override suspend fun getReside(): ApiResponse<ResideParent> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.getReside()
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun getIcon(): ApiResponse<Icon> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.getIcon()
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun postIcon(file: File): ApiResponse<ResponseIcon> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.postIcon(file.convertFileRequestBody)
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}