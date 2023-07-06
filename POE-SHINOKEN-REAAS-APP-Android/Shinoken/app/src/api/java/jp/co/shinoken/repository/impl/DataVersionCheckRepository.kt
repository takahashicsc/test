package jp.co.shinoken.repository.impl

import jp.co.shinoken.api.ShinokenBootApiService
import jp.co.shinoken.model.api.VersionCheck
import jp.co.shinoken.repository.VersionCheckRepository
import retrofit2.Response
import javax.inject.Inject

class DataVersionCheckRepository @Inject constructor(private val bootApiService: ShinokenBootApiService) :
    VersionCheckRepository {
    override suspend fun getVersionCheck(): Response<VersionCheck> {
        return bootApiService.getVersionCheck()
    }
}