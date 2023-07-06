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
import jp.co.shinoken.databinding.FragmentPasswordResetBinding
import jp.co.shinoken.extension.showSnackBar
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.ui.fragment.sign_up.SignUpCodeFormFragmentDirections
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PasswordResetFragment : Fragment() {
    private lateinit var binding: FragmentPasswordResetBinding
    private val viewModel: PasswordResetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentPasswordResetBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.passwordResetSendButton.setOnClickListener {
            viewModel.resetPassword()
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isButtonEnable.collect {
                binding.passwordResetSendButton.isEnabled = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.apiState.collect {
                binding.passwordResetProgressBar.hide()
                when (it) {
                    ApiState.Empty -> return@collect
                    ApiState.Success -> findNavController().navigate(
                        PasswordResetFragmentDirections.actionPasswordResetFragmentToPasswordResetInputFormFragment(
                            viewModel.email
                        )
                    )
                    ApiState.LOADING -> {
                        binding.passwordResetProgressBar.show()
                    }
                }
            }
        }

        binding.passwordResetEmailEdit.addTextChangedListener(PasswordResetTextWatcher(binding.passwordResetEmailEdit))
    }

    private inner class PasswordResetTextWatcher(private val view: View) :
        TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            val text = charSequence.toString()
            when (view.id) {
                binding.passwordResetEmailEdit.id -> viewModel.email = text
            }
            viewModel.validation()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    companion object {
        fun newInstance() = PasswordResetFragment()
    }
}