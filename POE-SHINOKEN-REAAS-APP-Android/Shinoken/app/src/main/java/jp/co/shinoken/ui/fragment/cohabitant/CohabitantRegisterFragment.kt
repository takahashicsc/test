package jp.co.shinoken.ui.fragment.cohabitant

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
import jp.co.shinoken.databinding.FragmentCohabitantRegisterBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.extension.hideKeyboard
import jp.co.shinoken.extension.showAlertDialog
import jp.co.shinoken.extension.showSnackBar
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.ui.widget.BirthDayInputDialogFragment
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CohabitantRegisterFragment : Fragment() {

    private val viewModel: CohabitantRegisterViewModel by viewModels()
    private lateinit var binding: FragmentCohabitantRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCohabitantRegisterBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cohabitantRequestButton.setOnClickListener {
            showAlertDialog(
                title = getString(R.string.cohabitant_form_dialog_title),
                message = getString(R.string.cohabitant_form_dialog_message),
                negativeText = getString(R.string.cancel),
                positiveListener = {
                    viewModel.postRoommate()
                }
            )
        }

        lifecycleScope.launchWhenCreated {
            viewModel.apiState.collect {
                binding.cohabitantRegisterProgressBar.hide()

                when (it) {
                    ApiState.Empty -> return@collect
                    ApiState.Success -> {
                        showSnackBar(
                            binding.cohabitantRequestButton,
                            getString(R.string.cohabitant_form_success)
                        )
                        findNavController().popBackStack()
                    }
                    ApiState.LOADING -> {
                        binding.cohabitantRegisterProgressBar.show()
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
            viewModel.isButtonEnable.collect {
                binding.cohabitantRequestButton.isEnabled = it
            }
        }

        binding.cohabitantFormBirthdayEdit.setOnClickListener {
            hideKeyboard()
            BirthDayInputDialogFragment(
                birthDay = viewModel.birthDay,
                listener = {
                    viewModel.birthDay = it
                    viewModel.validation()
                    binding.cohabitantFormBirthdayEdit.setText(
                        it.getBirthDayFormatString(
                            getString(
                                R.string.birth_day_format
                            )
                        )
                    )
                }
            ).show(
                requireActivity().supportFragmentManager,
                CohabitantRegisterFragment::class.java.simpleName
            )
        }

        binding.cohabitantFormMailEdit.apply {
            addTextChangedListener(
                CohabitantRegisterTextWatcher(
                    this
                )
            )
        }
        binding.cohabitantFormNameEdit.apply {
            addTextChangedListener(
                CohabitantRegisterTextWatcher(
                    this
                )
            )
        }
        binding.cohabitantFormBirthdayEdit.apply {
            addTextChangedListener(
                CohabitantRegisterTextWatcher(this)
            )
        }
    }

    private inner class CohabitantRegisterTextWatcher(private val view: View) :
        TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            val text = charSequence.toString()
            when (view.id) {
                binding.cohabitantFormMailEdit.id -> viewModel.email = text
                binding.cohabitantFormNameEdit.id -> viewModel.name = text
            }
            viewModel.validation()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    companion object {
        fun newInstance() = CohabitantRegisterFragment()
    }
}