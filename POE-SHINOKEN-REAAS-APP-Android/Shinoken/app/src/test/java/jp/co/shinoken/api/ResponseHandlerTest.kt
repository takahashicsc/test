package jp.co.shinoken.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.FaqCategory
import jp.co.shinoken.model.FaqContent
import jp.co.shinoken.model.Faqs
import jp.co.shinoken.model.api.Kind
import jp.co.shinoken.model.api.Link
import jp.co.shinoken.model.api.Status
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.HttpURLConnection


class ResponseHandlerTest {
    private val responseHandler = ResponseHandler()
    private var mockWebServer = MockWebServer()
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private lateinit var apiService: ShinokenApiService

    private val mockFaqContent =
        Faqs(
            total = 1,
            data = listOf(
                FaqContent(
                    id = 58,
                    title = "共用部に蜂の巣があります。",
                    slug = "450c9af6cea90e91",
                    contentText = "ガスメーターの安全装置がはたらき、ガスの供給が...",
                    categories = listOf(
                        FaqCategory(
                            id = 1,
                            name = "設備",
                            path = "/faq/cat/facility",
                            image = null,
                            status = Status.Opened,
                            serialCode = 0
                        )
                    ),
                    images = listOf(),
                    links = listOf(
                        Link(
                            kind = Kind.DeepLink,
                            title = "お客様のマイページ",
                            url = "shinoken-residentapp-demo:///mypage"
                        )
                    )
                ),
            ),
            categories = listOf()
        )

    @Before
    fun setUp() {
        mockWebServer.start()

        apiService = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(mockWebServer.url("/")) // note the URL is different from production one
            .build()
            .create(ShinokenApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `get_ResponseHandle_Success`() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(
                    "{\n" +
                            "  \"categories\": [],\n" +
                            "  \"total\": 1,\n" +
                            "  \"data\": [\n" +
                            "    {\n" +
                            "      \"id\": 58,\n" +
                            "      \"title\": \"共用部に蜂の巣があります。\",\n" +
                            "      \"slug\": \"450c9af6cea90e91\",\n" +
                            "      \"start_at\": \"2021-03-22T13:42:23.000Z\",\n" +
                            "      \"end_at\": \"2121-03-22T13:42:23.000Z\",\n" +
                            "      \"content_text\": \"ガスメーターの安全装置がはたらき、ガスの供給が...\",\n" +
                            "      \"images\": [],\n" +
                            "      \"labels\": [\n" +
                            "        {\n" +
                            "          \"id\": 1,\n" +
                            "          \"name\": \"設備\",\n" +
                            "          \"path\": \"/faq/cat/facility\",\n" +
                            "          \"image\": null,\n" +
                            "          \"status\": \"opened\",\n" +
                            "          \"serial_code\": 0\n" +
                            "        }\n" +
                            "      ],\n" +
                            "      \"links\": [\n" +
                            "        {\n" +
                            "          \"kind\": \"deeplink\",\n" +
                            "          \"title\": \"お客様のマイページ\",\n" +
                            "          \"url\": \"shinoken-residentapp-demo:///mypage\"\n" +
                            "        }\n" +
                            "      ],\n" +
                            "      \"categories\": [\n" +
                            "        {\n" +
                            "          \"id\": 1,\n" +
                            "          \"name\": \"設備\",\n" +
                            "          \"path\": \"/faq/cat/facility\",\n" +
                            "          \"image\": null,\n" +
                            "          \"status\": \"opened\",\n" +
                            "          \"serial_code\": 0\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}\n"
                )
        )
        runBlocking {
            val apiResponse = try {
                responseHandler.handleSuccess(
                    apiService.getFaqs()
                )
            } catch (e: Exception) {
                responseHandler.handleException(e)
            }
            assertEquals(
                ApiResponse(
                    status = jp.co.shinoken.api.Status.SUCCESS,
                    data = mockFaqContent,
                    appError = null
                ),
                apiResponse
            )
        }
    }

    @Test
    fun `get_ResponseHandle_handleException_HttpException`() {
        runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
            )

            val apiResponse = try {
                responseHandler.handleSuccess(
                    apiService.getFaqs()
                )
            } catch (e: Exception) {
                responseHandler.handleException(e)
            }

            assertTrue(
                apiResponse.appError is AppError.ApiException.AlertAppError
            )
        }
    }
}