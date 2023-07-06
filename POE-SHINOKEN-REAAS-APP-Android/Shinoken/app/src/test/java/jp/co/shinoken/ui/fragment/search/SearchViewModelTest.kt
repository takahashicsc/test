package jp.co.shinoken.ui.fragment.search

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import jp.co.shinoken.CoroutinesTestRule
import jp.co.shinoken.R
import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.SearchResult
import jp.co.shinoken.model.Tag
import jp.co.shinoken.repository.SearchRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

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

    private val mockSuccessRepository = mockk<SearchRepository> {
        // パターンの設定
        coEvery { sendSearch(any()) } returns ApiResponse(
            status = Status.SUCCESS,
            data = mockSearchResult,
            appError = null
        )
    }

    private val mockErrorRepository = mockk<SearchRepository> {
        // パターンの設定
        coEvery { sendSearch(any()) } returns ApiResponse(
            status = Status.ERROR,
            data = null,
            appError = AppError.ApiException.ApiOtherErrors(Throwable())
        )
    }

    @Test
    fun searchResultSuccess() = runBlockingTest {
        val viewModel = SearchViewModel(mockSuccessRepository)
        viewModel.search("BLUFF")
        coVerify { mockSuccessRepository.sendSearch("BLUFF") }
        assertEquals(2, viewModel.searchResult.value.tags.size)
    }

    @Test
    fun searchResultError() = runBlockingTest {
        val viewModel = SearchViewModel(mockErrorRepository)
        viewModel.search("BLUFF")
        coVerify { mockErrorRepository.sendSearch("BLUFF") }
        assertTrue(viewModel.appError.value is AppError.ApiException.ApiOtherErrors)
    }

}