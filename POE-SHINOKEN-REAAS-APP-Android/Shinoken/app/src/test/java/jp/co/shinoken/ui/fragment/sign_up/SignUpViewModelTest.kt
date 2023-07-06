package jp.co.shinoken.ui.fragment.sign_up

import jp.co.shinoken.CoroutinesTestRule
import jp.co.shinoken.repository.AuthRepository
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class SignUpViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    private val repository = mock(AuthRepository::class.java)

    @Test
    fun `isButtonEnable_入力欄全てでTrue`() = coroutineTestRule.testDispatcher.runBlockingTest {
        val viewModel = SignUpViewModel(repository)
        viewModel.name = "test"
        viewModel.mail = "test@test.com"
        viewModel.phoneNumber = "0000000000"
        viewModel.password = "*****"
        assertFalse(viewModel.isButtonEnable.value)
        viewModel.passwordConfirm = "*****"
        viewModel.validation()
        assertTrue(viewModel.isButtonEnable.value)
    }
}