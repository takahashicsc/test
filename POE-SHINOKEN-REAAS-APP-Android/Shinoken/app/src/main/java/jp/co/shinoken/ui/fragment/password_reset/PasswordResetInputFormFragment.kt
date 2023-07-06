package jp.co.shinoken.ui.fragment.password_reset

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentPasswordResetInputFormBinding
import jp.co.shinoken.extension.showAlertDialog
import jp.co.shinoken.extension.showSnackBar
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.ui.fragment.sign_up.SignUpFragmentDirections
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PasswordResetInputFormFragment : Fragment() {
    private lateinit var binding: FragmentPasswordResetInputFormBinding
    private val viewModel: PasswordResetInputFormViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentPasswordResetInputFormBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.confirmCodeEdit.addTextChangedListener(PasswordResetTextWatcher((binding.confirmCodeEdit)))
        binding.newPasswordEdit.addTextChangedListener(PasswordResetTextWatcher((binding.newPasswordEdit)))
        binding.newPasswordConfirmEdit.addTextChangedListener(PasswordResetTextWatcher((binding.newPasswordConfirmEdit)))

        binding.passwordResetChangeButton.setOnClickListener {
            viewModel.confirmResetPassword()
        }

        binding.resendCodeText.setOnClickListener {
            viewModel.resendCode()
            showAlertDialog(
                title = getString(R.string.code_form_resend_code_dialog_title),
                message = getString(R.string.code_form_resend_code_dialog_message)
            )
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isButtonEnable.collect {
                binding.passwordResetChangeButton.isEnabled = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.apiState.collect {
                binding.passwordResetInputFormProgressBar.hide()
                when (it) {
                    ApiState.Empty -> return@collect
                    ApiState.Success -> {
                        showSnackBar(
                            binding.passwordResetChangeButton,
                            getString(R.string.password_reset_input_form_success)
                        )
                        findNavController().navigate(PasswordResetInputFormFragmentDirections.actionPasswordResetInputFormFragmentToLoginFragment())
                    }
                    ApiState.LOADING -> {
                        binding.passwordResetInputFormProgressBar.show()
                    }
                }
            }
        }

        binding.passwordResetCodeFormDescriptionMailDescription.text =
            getString(R.string.code_form_description_mail_format).format(viewModel.userName)
    }

    private inner class PasswordResetTextWatcher(private val view: View) :
        TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            val text = charSequence.toString()
            when (view.id) {
                binding.confirmCodeEdit.id -> viewModel.code = text
                binding.newPasswordEdit.id -> viewModel.password = text
                binding.newPasswordConfirmEdit.id -> viewModel.passwordConfirm = text
            }
            viewModel.validation()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    companion object {
        fun newInstance() = PasswordResetInputFormFragment()
    }
}