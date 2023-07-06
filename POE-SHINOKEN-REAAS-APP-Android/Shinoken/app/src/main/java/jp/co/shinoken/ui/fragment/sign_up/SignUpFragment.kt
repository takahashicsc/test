package jp.co.shinoken.ui.fragment.sign_up

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
import jp.co.shinoken.databinding.FragmentSignUpBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.model.ApiState
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val viewModel: SignUpViewModel by viewModels()
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSignUpBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signUpCodeSend.setOnClickListener {
            viewModel.signUp()
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isButtonEnable.collect {
                binding.signUpCodeSend.isEnabled = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.apiState.collect {
                binding.signUpProgressBar.hide()
                when (it) {
                    ApiState.Empty -> return@collect
                    ApiState.Success -> {
                        findNavController().navigate(
                            SignUpFragmentDirections.actionSignUpFragmentToSignUpCodeFormFragment(
                                viewModel.mail,
                                viewModel.password
                            )
                        )
                        viewModel.setApiState(ApiState.Empty)
                    }
                    ApiState.LOADING -> {
                        binding.signUpProgressBar.show()
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.appError.collect {
                it ?: return@collect
                appError(it)
            }
        }

        binding.signUpNameEdit.addTextChangedListener(SignUpTextWatcher(binding.signUpNameEdit))
        binding.signUpEmailEdit.addTextChangedListener(SignUpTextWatcher(binding.signUpEmailEdit))
        binding.signUpTelPhoneEdit.addTextChangedListener(SignUpTextWatcher(binding.signUpTelPhoneEdit))
        binding.signUpPasswordEdit.addTextChangedListener(SignUpTextWatcher(binding.signUpPasswordEdit))
        binding.signUpPasswordConfirmEdit.addTextChangedListener(SignUpTextWatcher(binding.signUpPasswordConfirmEdit))
    }

    private inner class SignUpTextWatcher(private val view: View) :
        TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            val text = charSequence.toString()
            when (view.id) {
                binding.signUpNameEdit.id -> viewModel.name = text
                binding.signUpEmailEdit.id -> viewModel.mail = text
                binding.signUpTelPhoneEdit.id -> viewModel.phoneNumber = text
                binding.signUpPasswordEdit.id -> viewModel.password = text
                binding.signUpPasswordConfirmEdit.id -> viewModel.passwordConfirm = text
            }
            viewModel.validation()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    companion object {
        fun newInstance() = SignUpFragment()
    }
}