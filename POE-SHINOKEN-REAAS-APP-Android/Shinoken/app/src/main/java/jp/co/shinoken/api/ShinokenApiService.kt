package jp.co.shinoken.api

import jp.co.shinoken.api.request.RequestCheckForm
import jp.co.shinoken.api.request.RequestInquiryForm
import jp.co.shinoken.api.request.RequestRoomMate
import jp.co.shinoken.api.request.RequestRoomMateCancel
import jp.co.shinoken.api.response.ResponseIcon
import jp.co.shinoken.api.response.ResponseImage
import jp.co.shinoken.model.FaqDetail
import jp.co.shinoken.model.Faqs
import jp.co.shinoken.model.api.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface ShinokenApiService {
    @GET("residentapp/me")
    suspend fun getMe(): MeParent

    @GET("residentapp/home")
    suspend fun getHome(): HomeParent

    @GET("residentapp/resider")
    suspend fun getReside(): ResideParent

    @POST("push_device_tokens")
    suspend fun postPushToken(@Body pushToken: PushToken): Any

    @GET("icon")
    suspend fun getIcon(): Icon

    @Multipart
    @POST("icon")
    suspend fun postIcon(@Part iconImage: MultipartBody.Part): ResponseIcon

    @Multipart
    @POST("images")
    suspend fun postImage(
        @Part channel: MultipartBody.Part,
        @Part file: MultipartBody.Part,
    ): ResponseImage

    @GET("residentapp/boards")
    suspend fun getBoards(
        @Query("offset") offset: Int?
    ): Boards

    @GET("residentapp/boards/{id}")
    suspend fun getBoardDetail(@Path("id") id: Int): BoardDetail

    @GET("residentapp/notifications")
    suspend fun getNotifications(
        @Query("offset") offset: Int?
    ): Notifications

    @GET("residentapp/notifications/{id}")
    suspend fun getNotificationDetail(@Path("id") id: Int): NotificationDetail

    @GET("residentapp/faqs")
    suspend fun getFaqs(@Query("limit") limit: String = "1000"): Faqs

    @GET("residentapp/faqs/{id}")
    suspend fun getFaqs(@Path("id") id: Int): FaqDetail

    @PATCH("residentapp/notifications/{id}/read")
    suspend fun readNotification(@Path("id") id: Int): NotificationDetail

    @GET("residentapp/bills")
    suspend fun getBills(): Bills

    @GET("residentapp/bills/{month}")
    suspend fun getBillDetail(@Path("month") month: String): BillDetail

    @GET("residentapp/check_form")
    suspend fun getCheckForms(): CheckForms

    @PUT("residentapp/check_form/{slug}")
    suspend fun putCheckForm(
        @Path("slug") slug: String,
        @Body requestCheckForm: RequestCheckForm
    ): ResponseSuccessCheckForm

    @POST("residentapp/check_form/submit")
    suspend fun postCheckFormSubmit(
        @Body checkFormSubmit: CheckFormSubmit
    ): Any

    @GET("residentapp/manuals")
    suspend fun getManuals(): Manuals

    @GET("residentapp/benefits")
    suspend fun getBenefits(@Query("offset") offset: Int?): Benefits

    @GET("residentapp/inquiries")
    suspend fun getInquiries(): Inquiries

    @POST("residentapp/inquiries")
    suspend fun postInquiry(@Body requestInquiryForm: RequestInquiryForm): ResponseSuccessInquiry

    @POST("roommate_requests")
    suspend fun postRoomMateRequest(@Body requestRoomMate: RequestRoomMate): ResponseSuccessRoommate

    @GET("roommate_requests/{id}")
    suspend fun getRoomMateRequest(@Path("id") id: Int): RoommateDetail

    @HTTP(method = "DELETE", path = "/api/v1/roommate_requests/cancel", hasBody = true)
    suspend fun deleteRoomMateRequest(@Body requestRoomMateCancel: RequestRoomMateCancel): ResponseSuccessRoommate

    @GET("residentapp/settings")
    suspend fun getSettings(): Settings

    @PATCH("residentapp/settings/subresident_billing_acl/allow")
    suspend fun patchSubresidentBillingActive(): ResponseSettings

    @PATCH("residentapp/settings/subresident_billing_acl/deny")
    suspend fun patchSubresidentBillingInActive(): ResponseSettings
}