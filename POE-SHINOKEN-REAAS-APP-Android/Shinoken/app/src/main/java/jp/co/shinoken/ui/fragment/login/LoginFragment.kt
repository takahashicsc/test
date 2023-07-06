package jp.co.shinoken.ui.fragment.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.BuildConfig
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentLoginBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.extension.showAlertDialog
import jp.co.shinoken.extension.showChromeCustomTabs
import jp.co.shinoken.model.ApiState
import kotlinx.coroutines.flow.collect
import timber.log.Timber


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentLoginBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetch()

        binding.signInButton.setOnClickListener {
            viewModel.signIn()
        }

        binding.showPasswordReset.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToPasswordResetFragment())
        }

        binding.showSignInSupport.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignInSupportFragment())
        }

        binding.signUpButton.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }

        lifecycleScope.launchWhenCreated {
            viewModel.termsUrl.collect { termsUri ->
                binding.showTerms.run {
                    isVisible = termsUri != null
                    termsUri ?: return@collect
                    setOnClickListener {
                        showChromeCustomTabs(termsUri.toUri())
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isButtonEnable.collect {
                binding.signInButton.isEnabled = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.apiState.collect {
                binding.signInProgressBar.hide()
                when (it) {
                    ApiState.Empty -> return@collect
                    ApiState.Success -> {
                        if (findNavController().currentDestination?.id != R.id.loginFragment) return@collect
                        findNavController().navigate(
                            LoginFragmentDirections.actionLoginFragmentToHomeFragment(
                                isReload = true
                            )
                        )
                    }
                    ApiState.LOADING -> {
                        binding.signInProgressBar.show()
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isNotExistsUser.collect {
                if (it.not()) return@collect
                showAlertDialog(
                    title = getString(R.string.sign_in_error_user_not_exists_title),
                    message = getString(R.string.sign_in_error_user_not_exists_message)
                )
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.appError.collect {
                it ?: return@collect
                appError(it)
            }
        }


        binding.emailEdit.apply { addTextChangedListener(LoginTextWatcher(this)) }
        binding.passwordEdit.apply { addTextChangedListener(LoginTextWatcher(this)) }

        if (BuildConfig.FLAVOR == "staging" && BuildConfig.DEBUG) {
            binding.emailEdit.setText("shinoken-staging+user1@googlegroups.com")
            binding.passwordEdit.setText("aaaaAAAA@1")
        }
    }

    private inner class LoginTextWatcher(private val view: View) :
        TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            val text = charSequence.toString()
            when (view.id) {
                binding.passwordEdit.id -> viewModel.password = text
                binding.emailEdit.id -> viewModel.email = text
            }
            viewModel.validation()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}