package jp.co.shinoken.repository.impl

import jp.co.shinoken.R
import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.SearchResult
import jp.co.shinoken.model.Tag
import jp.co.shinoken.repository.SearchRepository
import javax.inject.Inject

class DataSearchRepository @Inject constructor() : SearchRepository {
    private val mockSearchResult =
        SearchResult(
            tags = listOf(
                Tag(
                    "契約内容の確認",
                    R.id.contractUpdateFragment
                ),
                Tag(
                    "家財保険について",
                    R.id.insuranceFragment
                )
            )
        )

    override suspend fun sendSearch(searchText: String): ApiResponse<SearchResult> {
        return ApiResponse(
            status = Status.SUCCESS,
            data = mockSearchResult,
            appError = null
        )
    }
}