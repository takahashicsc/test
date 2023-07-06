package jp.co.shinoken.ui.fragment.password_reset

import jp.co.shinoken.CoroutinesTestRule
import jp.co.shinoken.repository.AuthRepository
import junit.framework.TestCase
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class PasswordResetViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    private val repository = Mockito.mock(AuthRepository::class.java)

    @Test
    fun `isButtonEnable_入力欄全てでTrue`() = coroutineTestRule.testDispatcher.runBlockingTest {
        val viewModel = PasswordResetViewModel(repository)
        TestCase.assertFalse(viewModel.isButtonEnable.value)
        viewModel.email = "123445"
        viewModel.validation()
        TestCase.assertTrue(viewModel.isButtonEnable.value)
    }
}