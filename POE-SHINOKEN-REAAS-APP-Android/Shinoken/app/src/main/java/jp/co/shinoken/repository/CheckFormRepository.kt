package jp.co.shinoken.repository

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.request.RequestCheckForm
import jp.co.shinoken.api.response.ResponseImage
import jp.co.shinoken.model.api.CheckForms
import jp.co.shinoken.model.api.ResponseSuccessCheckForm
import java.io.File

interface CheckFormRepository {
    suspend fun getCheckForms(): ApiResponse<CheckForms>
    suspend fun postImage(file: File): ApiResponse<ResponseImage>
    suspend fun putCheckForm(
        slug: String,
        requestCheckForms: RequestCheckForm
    ): ApiResponse<ResponseSuccessCheckForm>


    suspend fun postCheckFormSubmit(isCheck: Boolean): ApiResponse<Any>
}