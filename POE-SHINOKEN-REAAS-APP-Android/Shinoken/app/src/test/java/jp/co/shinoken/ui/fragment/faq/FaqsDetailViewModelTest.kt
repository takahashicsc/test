package jp.co.shinoken.ui.fragment.faq

import androidx.lifecycle.SavedStateHandle
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import jp.co.shinoken.CoroutinesTestRule
import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.FaqCategory
import jp.co.shinoken.model.FaqContent
import jp.co.shinoken.model.FaqDetail
import jp.co.shinoken.model.api.Status
import jp.co.shinoken.repository.FaqRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FaqsDetailViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    private val mockFaqContent = FaqDetail(
        FaqContent(
            id = 1,
            title = "共用部に蜂の巣があります。",
            slug = "1476bbf143e4dfe9",
            contentText = "ガスメーターの安全装置がはたらき、ガスの供給が...",
            listOf(),
            categories = listOf(
                FaqCategory(
                    id = 1,
                    name = "共用部",
                    path = "",
                    image = null,
                    status = Status.Opened,
                    serialCode = 0
                )
            ),
            links = listOf()

        )
    )

    private val savedStateHandle = SavedStateHandle()

    private val mockSuccessRepository = mockk<FaqRepository> {
        // パターンの設定
        coEvery { getFaqDetail(1) } returns ApiResponse(
            status = jp.co.shinoken.api.Status.SUCCESS,
            data = mockFaqContent,
            appError = null
        )
    }

    private val mockErrorRepository = mockk<FaqRepository> {
        // パターンの設定
        coEvery { getFaqDetail(1) } returns ApiResponse(
            status = jp.co.shinoken.api.Status.ERROR,
            data = null,
            appError = AppError.ApiException.ApiOtherErrors(Throwable())
        )
    }

    @Before
    fun setUp() {
        savedStateHandle.set("faq_id", 1)
    }

    @Test
    fun fetchSuccess() = runBlockingTest {
        val viewModel = FaqDetailViewModel(mockSuccessRepository, savedStateHandle)
        viewModel.fetch()
        coVerify { mockSuccessRepository.getFaqDetail(1) }
        assertEquals(mockFaqContent.data, viewModel.faqDetail.value)
    }
}