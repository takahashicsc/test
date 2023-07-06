package jp.co.shinoken.repository

import jp.co.shinoken.model.api.VersionCheck
import retrofit2.Response

interface VersionCheckRepository {
    suspend fun getVersionCheck(): Response<VersionCheck>
}