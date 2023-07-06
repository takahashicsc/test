package jp.co.shinoken.repository.impl

import jp.co.shinoken.api.ShinokenBootApiService
import jp.co.shinoken.api.aws.AmplifyClient
import jp.co.shinoken.model.StubConstants
import jp.co.shinoken.repository.AuthRepository
import javax.inject.Inject

class DataAuthRepository @Inject constructor(
    private val amplifyClient: AmplifyClient,
    private val shinokenBootApiService: ShinokenBootApiService
) : AuthRepository {
    override fun sampleMethod() {
        amplifyClient.signIn()
    }

    override suspend fun tokenRefresh(refreshToken: String): String {
        return StubConstants.apiToken
    }

}