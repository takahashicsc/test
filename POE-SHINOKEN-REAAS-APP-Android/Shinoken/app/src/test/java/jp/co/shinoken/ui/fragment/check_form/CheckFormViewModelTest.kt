package jp.co.shinoken.ui.fragment.check_form

import androidx.lifecycle.SavedStateHandle
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import jp.co.shinoken.CoroutinesTestRule
import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.CheckFormResult
import jp.co.shinoken.model.api.CheckForm
import jp.co.shinoken.model.api.CheckForms
import jp.co.shinoken.repository.CheckFormRepository
import junit.framework.TestCase.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CheckFormViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    private val mockCheckItems =
        CheckForms(
            data = listOf(
                CheckForm(
                    slug = "opening-intercom",
                    category = "opening",
                    title = "玄関ドア",
                    result = null,
                    checks = listOf(CheckForm.Check(point = "", by = "")),
                    description = "",
                    images = listOf()
                ),
                CheckForm(
                    slug = "b",
                    category = "opening",
                    title = "インターホン",
                    result = null, checks = listOf(CheckForm.Check(point = "", by = "")),
                    description = "",
                    images = listOf()
                ),
            ),
            categories = listOf(
                CheckForms.Category(
                    slug = "opening",
                    title = "玄関・廊下"
                ),
                CheckForms.Category(
                    slug = "kitchen-kitchen",
                    title = "キッチン"
                ),
                CheckForms.Category(
                    slug = "washing",
                    title = "脱衣"
                )
            ),
            submittable = false,
            deadline = "",
        )


    private val savedStateHandle = SavedStateHandle()

    private val mockSuccessRepository = mockk<CheckFormRepository> {
        // パターンの設定
        coEvery { getCheckForms() } returns ApiResponse(
            status = Status.SUCCESS,
            data = mockCheckItems,
            appError = null
        )
    }

    private val mockErrorRepository = mockk<CheckFormRepository> {
        // パターンの設定
        coEvery { getCheckForms() } returns ApiResponse(
            status = Status.ERROR,
            data = null,
            appError = AppError.ApiException.ApiOtherErrors(Throwable())
        )
    }

    @Before
    fun setUp() {
        savedStateHandle.set(CheckFormFragment.ArgIsCheckedForm, false)
    }

    @Test
    fun fetchSuccess() = runBlockingTest {
        val viewModel = CheckFormViewModel(savedStateHandle, mockSuccessRepository)
        viewModel.fetch()
        coVerify { mockSuccessRepository.getCheckForms() }
        assertEquals(3, viewModel.formCheckItem.value.size)
        assertEquals(2, viewModel.formCheckItem.value.first().checkItems.size)
    }

    @Test
    fun fetchResultError() = runBlockingTest {
        val viewModel = CheckFormViewModel(savedStateHandle, mockErrorRepository)
        viewModel.fetch()
        coVerify { mockErrorRepository.getCheckForms() }
        assertTrue(viewModel.appError.value is AppError.ApiException.ApiOtherErrors)
    }

    @Test
    fun fetchIsSaved() = runBlockingTest {
        val viewModel = CheckFormViewModel(savedStateHandle, mockSuccessRepository)
        viewModel.fetch()
        coVerify { mockSuccessRepository.getCheckForms() }

        savedStateHandle.set(CheckFormFragment.ArgIsCheckedForm, true)
        val newViewModel = CheckFormViewModel(savedStateHandle, mockSuccessRepository)


        assertFalse(viewModel.isCheckedForm == newViewModel.isCheckedForm)
    }


    @Test
    fun updateText() = runBlockingTest {
        val viewModel = CheckFormViewModel(savedStateHandle, mockSuccessRepository)
        viewModel.fetch()
        coVerify { mockSuccessRepository.getCheckForms() }
        val checkItem = viewModel.formCheckItem.value[0]
        viewModel.updateText("BLUFF", checkItem.checkItems.first())
        assertEquals(viewModel.formCheckItem.value[0].checkItems.get(0).description, "BLUFF")
    }

    @Test
    fun updateCheck() = runBlockingTest {
        val viewModel = CheckFormViewModel(savedStateHandle, mockSuccessRepository)
        viewModel.fetch()
        coVerify { mockSuccessRepository.getCheckForms() }
        val checkItem = viewModel.formCheckItem.value[0]
        checkItem.checkItems[0].result = CheckFormResult.OK
        viewModel.updateCheck(checkItem.checkItems[0])

        assertEquals(viewModel.formCheckItem.value[0].checkItems.get(0).result, CheckFormResult.OK)
    }

    @Test
    fun checkItemSlug() = runBlockingTest {
        val viewModel = CheckFormViewModel(savedStateHandle, mockSuccessRepository)
        assertEquals(viewModel.checkItemSlug, "opening-intercom")
    }
}