package jp.co.shinoken.api

import jp.co.shinoken.BuildConfig
import jp.co.shinoken.api.request.RequestInquiryForm
import jp.co.shinoken.api.request.RequestSignInSupport
import jp.co.shinoken.model.api.*
import retrofit2.Response
import retrofit2.http.*

interface ShinokenBootApiService {
    @POST("api/v1/refresh")
    suspend fun tokenRefresh(
        @Header("Authorization") authorization: String
    ): TokenRefresh

    @GET("api/v1/residentapp/meta/version_check")
    suspend fun getVersionCheck(@Header("Application") application: String = "android=${BuildConfig.VERSION_NAME}"): Response<VersionCheck>

    @GET("bootapi/v1/{client_name}/user")
    suspend fun getUserInfo(
        @Header("Authorization") authorization: String,
        @Header("Application") application: String = "Android=${BuildConfig.VERSION_NAME}",
        @Path("client_name") clientName: String = BootApiConstants.ClientName
    ): UserInfo

    @POST("bootapi/v1/{client_name}/authorize/{auth_id}")
    suspend fun authorizeExecute(
        @Header("Authorization") authorization: String,
        @Header("Application") application: String = "Android=${BuildConfig.VERSION_NAME}",
        @Path("client_name") clientName: String = BootApiConstants.ClientName,
        @Path("auth_id") authId: String,
        @Query("device_id") deviceId: String
    ): TokenRefresh

    @GET("api/v1/residentapp/meta")
    suspend fun getMeta(): Me.MetaParent

    @POST("api/v1/residentapp/inquiries")
    suspend fun postSignInSupport(@Body signInSupport: RequestSignInSupport): ResponseSuccessInquiry
}

object BootApiConstants {
    const val ClientName: String = "residentapp"
}