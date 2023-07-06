package jp.co.shinoken.ui.fragment.sign_in_support

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentSignInSupportMailFormBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.extension.hideKeyboard
import jp.co.shinoken.extension.showChromeCustomTabs
import jp.co.shinoken.extension.showSnackBar
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.ui.fragment.warning_dialog.WarningDialogFragment
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignInSupportMailFormFragment : Fragment() {
    private val viewModel: SignInSupportMailFormViewModel by viewModels()
    private lateinit var binding: FragmentSignInSupportMailFormBinding
    private var isShowDialog: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSignInSupportMailFormBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signInSupportMailFormSendButton.setOnClickListener {
            hideKeyboard()
            showWarningDialog()
        }

        lifecycleScope.launchWhenCreated {
            viewModel.apiState.collect {
                if (it == ApiState.Success) {
                    showSnackBar(
                        binding.signInSupportMailFormSendButton,
                        getString(R.string.sign_in_support_send_success_message)
                    )
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
            viewModel.isButtonEnable.collect {
                binding.signInSupportMailFormSendButton.isEnabled = it
            }
        }

        binding.signInSupportMailFormNameEdit.addTextChangedListener(
            SignInSupportMailFormTextWatcher(binding.signInSupportMailFormNameEdit)
        )
        binding.signInSupportMailFormNameKanaEdit.addTextChangedListener(
            SignInSupportMailFormTextWatcher(binding.signInSupportMailFormNameKanaEdit)
        )
        binding.signInSupportMailFormContractPhoneNumberEdit.addTextChangedListener(
            SignInSupportMailFormTextWatcher(binding.signInSupportMailFormContractPhoneNumberEdit)
        )
        binding.signInSupportMailFormContractMailEdit.addTextChangedListener(
            SignInSupportMailFormTextWatcher(binding.signInSupportMailFormContractMailEdit)
        )
        binding.signInSupportMailFormContentEdit.addTextChangedListener(
            SignInSupportMailFormTextWatcher(binding.signInSupportMailFormContentEdit)
        )
    }

    private inner class SignInSupportMailFormTextWatcher(private val view: View) :
        TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            val text = charSequence.toString()
            when (view.id) {
                binding.signInSupportMailFormNameEdit.id -> viewModel.name = text
                binding.signInSupportMailFormNameKanaEdit.id -> viewModel.kana = text
                binding.signInSupportMailFormContractPhoneNumberEdit.id -> viewModel.phoneNumber =
                    text
                binding.signInSupportMailFormContractMailEdit.id -> viewModel.mail = text
                binding.signInSupportMailFormContentEdit.id -> viewModel.content = text
            }
            viewModel.validation()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    override fun onResume() {
        super.onResume()
        if (isShowDialog) {
            showWarningDialog()
            isShowDialog = false
        }
    }

    private fun showWarningDialog() {
        WarningDialogFragment(
            title = getString(R.string.sign_in_support_warning_title),
            message = getString(R.string.sign_in_support_warning_message),
            buttonText = getString(R.string.sign_in_support_warning_button_text),
            underLineText = getString(R.string.sign_in_support_warning_under_line_text)
        ).apply {
            setButtonClickListener {
                viewModel.postSignInSupport()
            }
            setUnderLineTextClickListener {
                isShowDialog = true
                viewModel.privacyPolicyUrl.value?.let {
                    showChromeCustomTabs(it.toUri())
                }
            }
        }.show(
            requireActivity().supportFragmentManager,
            SignInSupportMailFormFragment::class.java.simpleName
        )
    }

    companion object {
        fun newInstance() = SignInSupportMailFormFragment()
    }
}