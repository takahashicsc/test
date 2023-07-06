package jp.co.shinoken.di

import com.serjltt.moshi.adapters.SerializeNulls
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.shinoken.BuildConfig
import jp.co.shinoken.api.ShinokenApiService
import jp.co.shinoken.api.ShinokenBootApiService
import jp.co.shinoken.api.Status
import jp.co.shinoken.repository.AuthRepository
import jp.co.shinoken.repository.StoreRepository
import jp.co.shinoken.repository.UserInfoRepository
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideShinokenApiService(
        okHttpClient: OkHttpClient
    ): ShinokenApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(SerializeNulls.ADAPTER_FACTORY).build()
                )
            )
            .client(okHttpClient)
            .build()
            .create(ShinokenApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideShinokenBootApiService(): ShinokenBootApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BOOT_API_BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(Moshi.Builder().build())
            )
            .client(getOkHttpBuilder().build())
            .build()
            .create(ShinokenBootApiService::class.java)
    }


    class RequestInterceptor(private val storeRepository: StoreRepository) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var newRequest: Request = chain.request()

            val token = "Bearer ${storeRepository.getApiToken()}"

            newRequest = newRequest.newBuilder()
                .addHeader(
                    "Authorization",
                    token,
                )
                .addHeader("Accept", "application/json")
                .build()

            Timber.tag("OkHttpClient").d(
                String.format(
                    "--> Sending request %s on %s%n%s",
                    newRequest.url,
                    chain.connection(),
                    newRequest.headers
                )
            )

            return chain.proceed(newRequest)
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        storeRepository: StoreRepository,
        authRepository: AuthRepository,
        userInfoRepository: UserInfoRepository
    ): OkHttpClient {
        val builder = getOkHttpBuilder()
        builder.addInterceptor(RequestInterceptor(storeRepository))
        builder.authenticator(
            TokenAuthenticator(
                storeRepository,
                userInfoRepository
            )
        )
        builder.connectTimeout(30, TimeUnit.SECONDS)
        builder.writeTimeout(30, TimeUnit.SECONDS)
        builder.readTimeout(30, TimeUnit.SECONDS)
        return builder.build()
    }

    private fun getOkHttpBuilder(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        return builder
    }

    class TokenAuthenticator(
        private val storeRepository: StoreRepository,
        private val userInfoRepository: UserInfoRepository
    ) : Authenticator {
        private var count = 1
        override fun authenticate(route: Route?, response: Response): Request? {
            // 2 回リトライを行う。 return null で authenticate メソッドの loop から抜ける
            if (this.retryCount(response = response) > 2) {
                return null
            }

            // アプリ内で保持している refreshToken
            val refreshToken = storeRepository.getApiRefreshToken()

            // refreshToken を利用して token を更新する
            refreshToken ?: return null
            val newToken = this.updateToken() ?: return null

            return response.request.newBuilder().header("Authorization", "Bearer $newToken")
                .build()
        }

        private fun updateToken(): String? {
            val refreshToken = storeRepository.getApiRefreshToken() ?: return null
            val refreshTokenResponse =
                runBlocking { userInfoRepository.tokenRefresh(refreshToken) }

            if (refreshTokenResponse.status != Status.SUCCESS) return null
            val accessToken = refreshTokenResponse.data!!.data.accessToken

            runBlocking {
                storeRepository.saveApiToken(accessToken)
            }
            return accessToken
        }

        private fun retryCount(response: Response): Int {
            response.priorResponse?.let {
                count += 1
            }
            return count
        }
    }
}