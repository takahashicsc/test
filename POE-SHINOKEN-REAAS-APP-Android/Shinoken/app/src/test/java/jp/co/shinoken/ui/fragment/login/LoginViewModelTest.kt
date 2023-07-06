package jp.co.shinoken.ui.fragment.login

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import jp.co.shinoken.CoroutinesTestRule
import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.api.Me
import jp.co.shinoken.repository.AuthRepository
import jp.co.shinoken.repository.MetaRepository
import jp.co.shinoken.repository.StoreRepository
import jp.co.shinoken.repository.UserInfoRepository
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class LoginViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    private val mockMeta = Me.MetaParent(
        Me.Meta(
            termOfService = "https://shinoken.reivo.info/pages/terms-of-service",
            privacyPolicy = "https://shinoken.reivo.info/pages/privacy-policy",
            mailTransfer = "https://www.post.japanpost.jp/service/tenkyo/index.html",
            cancelRequest = ""
        )
    )

    private val repository = mock(AuthRepository::class.java)
    private val storeRepository = mock(StoreRepository::class.java)
    private val userInfoRepository = mock(UserInfoRepository::class.java)
    private val mockMetaRepository = mockk<MetaRepository> {
        coEvery { getMeta() } returns ApiResponse(
            status = Status.SUCCESS,
            data = mockMeta,
            appError = null
        )
    }

    @Test
    fun `isButtonEnable_メールアドレスとパスワード入力でTrue`() = coroutineTestRule.testDispatcher.runBlockingTest {
        val viewModel =
            LoginViewModel(repository, userInfoRepository, storeRepository, mockMetaRepository)
        viewModel.password = "test"
        viewModel.email = "example@example.com"
        viewModel.validation()
        assertThat(viewModel.password, `is`("test"))
        assertThat(viewModel.email, `is`("example@example.com"))
        assertTrue(viewModel.isButtonEnable.value)
    }

    @Test
    fun `isButtonEnable_メールアドレス入力のみではFalse`() = coroutineTestRule.testDispatcher.runBlockingTest {
        val viewModel =
            LoginViewModel(repository, userInfoRepository, storeRepository, mockMetaRepository)
        viewModel.email = "example@example.com"
        viewModel.validation()
        assertThat(viewModel.password, `is`(""))
        assertThat(viewModel.email, `is`("example@example.com"))
        assertFalse(viewModel.isButtonEnable.value)
    }
}