package jp.co.shinoken.ui.fragment.password_reset

import androidx.lifecycle.SavedStateHandle
import jp.co.shinoken.CoroutinesTestRule
import jp.co.shinoken.repository.AuthRepository
import jp.co.shinoken.ui.fragment.sign_up.SignUpCodeFormViewModel
import junit.framework.TestCase
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class PasswordResetInputFormViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    private val repository = Mockito.mock(AuthRepository::class.java)

    private val savedStateHandle = SavedStateHandle()

    @Test
    fun `isButtonEnable_入力欄全てでTrue`() = coroutineTestRule.testDispatcher.runBlockingTest {
        val viewModel = PasswordResetInputFormViewModel(repository, savedStateHandle)
        TestCase.assertFalse(viewModel.isButtonEnable.value)
        viewModel.code = "123445"
        viewModel.password = "****"
        viewModel.passwordConfirm = "****"
        viewModel.validation()
        TestCase.assertTrue(viewModel.isButtonEnable.value)
    }
}