package jp.co.shinoken.repository

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.model.SearchResult

interface SearchRepository {
    suspend fun sendSearch(searchText: String): ApiResponse<SearchResult>
}