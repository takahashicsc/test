package jp.co.shinoken.ui.fragment.sign_in_support

import jp.co.shinoken.CoroutinesTestRule
import jp.co.shinoken.repository.AuthRepository
import jp.co.shinoken.repository.MetaRepository
import junit.framework.TestCase
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class SignInSupportMailFormViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    private val authRepository = Mockito.mock(AuthRepository::class.java)

    private val metaRepository = Mockito.mock(MetaRepository::class.java)


    @Test
    fun `isButtonEnable_入力欄全てでTrue`() = coroutineTestRule.testDispatcher.runBlockingTest {
        val viewModel = SignInSupportMailFormViewModel(authRepository, metaRepository)
        viewModel.name = "name"
        viewModel.kana = "kana"
        viewModel.phoneNumber = "00000"
        viewModel.mail = "test@test.com"
        TestCase.assertFalse(viewModel.isButtonEnable.value)
        viewModel.content = "aaaa"
        viewModel.validation()
        TestCase.assertTrue(viewModel.isButtonEnable.value)
    }
}