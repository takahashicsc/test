package jp.co.shinoken.repository

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.response.ResponseIcon
import jp.co.shinoken.model.api.*
import java.io.File

interface HomeRepository {
    suspend fun getHome(): ApiResponse<Home>

    suspend fun getMe(): ApiResponse<MeParent>

    suspend fun getReside(): ApiResponse<ResideParent>

    suspend fun getIcon(): ApiResponse<Icon>

    suspend fun postIcon(file: File): ApiResponse<ResponseIcon>
}