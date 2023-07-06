package jp.co.shinoken.repository.impl

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.ResponseHandler
import jp.co.shinoken.api.ShinokenApiService
import jp.co.shinoken.api.request.RequestCheckForm
import jp.co.shinoken.api.response.ResponseImage
import jp.co.shinoken.extension.convertImageRequestBody
import jp.co.shinoken.model.api.CheckFormSubmit
import jp.co.shinoken.model.api.CheckForms
import jp.co.shinoken.model.api.ResponseSuccessCheckForm
import jp.co.shinoken.repository.CheckFormRepository
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject


class DataCheckFormRepository @Inject constructor(private val shinokenApiService: ShinokenApiService) :
    CheckFormRepository {
    private val responseHandler = ResponseHandler()

    override suspend fun getCheckForms(): ApiResponse<CheckForms> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.getCheckForms()
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun postImage(file: File): ApiResponse<ResponseImage> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.postImage(
                    channel = MultipartBody.Part.createFormData("channel", "check_form"),
                    file = file.convertImageRequestBody
                )
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun putCheckForm(
        slug: String,
        requestCheckForms: RequestCheckForm
    ): ApiResponse<ResponseSuccessCheckForm> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.putCheckForm(slug = slug, requestCheckForm = requestCheckForms)
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun postCheckFormSubmit(isCheck: Boolean): ApiResponse<Any> {
        return try {
            responseHandler.handleSuccess(
                shinokenApiService.postCheckFormSubmit(CheckFormSubmit(repairmentRequired = isCheck))
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}