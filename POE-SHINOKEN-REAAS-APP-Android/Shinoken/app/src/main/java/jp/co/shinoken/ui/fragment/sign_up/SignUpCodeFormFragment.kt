package jp.co.shinoken.ui.fragment.sign_up

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.activity.SignUpErrorActivity
import jp.co.shinoken.databinding.FragmentSignUpCodeFormBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.extension.showAlertDialog
import jp.co.shinoken.extension.showSnackBar
import jp.co.shinoken.model.ApiState
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignUpCodeFormFragment : Fragment() {

    private val viewModel: SignUpCodeFormViewModel by viewModels()
    private lateinit var binding: FragmentSignUpCodeFormBinding

    private lateinit var listener: ISignUpCodeFormFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSignUpCodeFormBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpCodeFormButton.setOnClickListener {
            viewModel.confirmSignUp()
        }

        binding.resendSignUpCodeText.setOnClickListener {
            viewModel.resendSignUpCode()
            showAlertDialog(
                title = getString(R.string.code_form_resend_code_dialog_title),
                message = getString(R.string.code_form_resend_code_dialog_message)
            )
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isButtonEnable.collect {
                binding.signUpCodeFormButton.isEnabled = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.apiState.collect {
                binding.signUpCodeFormProgressBar.hide()
                binding.signUpCodeFormButton.isEnabled = true
                when (it) {
                    ApiState.Empty -> return@collect
                    ApiState.Success -> {
                        showSnackBar(
                            binding.signUpCodeFormButton,
                            getString(R.string.sign_up_code_form_success)
                        )
                        findNavController().navigate(SignUpCodeFormFragmentDirections.actionSignUpCodeFormFragmentToHomeFragment(isReload = true))
                    }
                    ApiState.LOADING -> {
                        binding.signUpCodeFormProgressBar.show()
                        binding.signUpCodeFormButton.isEnabled = false
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

        lifecycleScope.launchWhenCreated {
            viewModel.isNotExistsUser.collect {
                if (it.not()) return@collect
                listener.showSignUpErrorActivity()
            }
        }

        binding.signUpCodeFormDescriptionMail.text =
            getString(R.string.code_form_description_mail_format).format(viewModel.userName)


        binding.signUpCodeFormEdit.addTextChangedListener(SignUpCodeFormTextWatcher(binding.signUpCodeFormEdit))
    }

    private inner class SignUpCodeFormTextWatcher(private val view: View) :
        TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            val text = charSequence.toString()
            when (view.id) {
                binding.signUpCodeFormEdit.id -> viewModel.code = text
            }
            viewModel.validation()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ISignUpCodeFormFragment) {
            listener = context
        }
    }

    interface ISignUpCodeFormFragment {
        fun showSignUpErrorActivity()
    }

    companion object {
        fun newInstance() = SignUpCodeFormFragment()
    }
}