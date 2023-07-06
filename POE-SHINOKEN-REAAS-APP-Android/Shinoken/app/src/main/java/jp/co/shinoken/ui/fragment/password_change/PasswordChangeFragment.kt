package jp.co.shinoken.ui.fragment.password_change

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentPasswordChangeBinding
import jp.co.shinoken.extension.hideKeyboard
import jp.co.shinoken.extension.showSnackBar
import jp.co.shinoken.model.ApiState
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PasswordChangeFragment : Fragment() {

    private lateinit var binding: FragmentPasswordChangeBinding

    private val viewModel: PasswordChangeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentPasswordChangeBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.passwordChangeButton.setOnClickListener {
            hideKeyboard()
            viewModel.updatePassword()
        }
        lifecycleScope.launchWhenCreated {
            viewModel.isButtonEnable.collect {
                binding.passwordChangeButton.isEnabled = it
            }
        }

        binding.passwordChangeProgressBar.hide()

        lifecycleScope.launchWhenCreated {
            viewModel.apiState.collect {
                binding.passwordChangeProgressBar.hide()
                when (it) {
                    ApiState.Empty -> return@collect
                    ApiState.Success -> {
                        showSnackBar(
                            binding.passwordChangeButton,
                            getString(R.string.password_change_success)
                        )
                    }
                    ApiState.LOADING -> {
                        binding.passwordChangeProgressBar.show()
                    }
                }
            }
        }

        binding.oldPasswordEdit.addTextChangedListener(PasswordResetTextWatcher(binding.oldPasswordEdit))
        binding.newPasswordEdit.addTextChangedListener(PasswordResetTextWatcher(binding.newPasswordEdit))
        binding.newPasswordConfirmEdit.addTextChangedListener(PasswordResetTextWatcher(binding.newPasswordConfirmEdit))
    }

    private inner class PasswordResetTextWatcher(private val view: View) :
        TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            val text = charSequence.toString()
            when (view.id) {
                binding.oldPasswordEdit.id -> viewModel.oldPassword = text
                binding.newPasswordEdit.id -> viewModel.newPassword = text
                binding.newPasswordConfirmEdit.id -> viewModel.newPasswordConfirm = text
            }
            viewModel.validation()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    companion object {
        fun newInstance() = PasswordChangeFragment()
    }
}