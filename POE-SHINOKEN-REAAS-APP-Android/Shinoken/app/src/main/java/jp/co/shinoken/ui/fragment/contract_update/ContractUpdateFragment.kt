package jp.co.shinoken.ui.fragment.contract_update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentContractUpdateBinding
import jp.co.shinoken.extension.showAlertDialog
import jp.co.shinoken.extension.showChromeCustomTabs
import jp.co.shinoken.ui.fragment.warning_dialog.WarningDialogFragment

@AndroidEntryPoint
class ContractUpdateFragment : Fragment() {

    private val viewModel: ContractUpdateViewModel by viewModels()
    private lateinit var binding: FragmentContractUpdateBinding
    private var isShowDialog: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentContractUpdateBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.contractUpdateButton.setOnClickListener {
            showWarningDialog()
        }
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
            title = getString(R.string.contract_update_dialog_title),
            message = getString(R.string.contract_update_dialog_message),
            buttonText = getString(R.string.contract_update_dialog_button_text),
            underLineText = getString(R.string.contract_update_dialog_under_line_text)
        ).apply {
            setButtonClickListener {
                showAlertDialog(
                    title = getString(R.string.contract_update_success_dialog_title),
                    message = getString(R.string.contract_update_success_dialog_message)
                )
            }
            setUnderLineTextClickListener {
                isShowDialog = true
                viewModel.privacyPolicyUrl.value?.let {
                    showChromeCustomTabs(it.toUri())
                }
            }
        }.show(
            requireActivity().supportFragmentManager,
            ContractUpdateFragment::class.java.simpleName
        )
    }

    companion object {
        fun newInstance() = ContractUpdateFragment()
    }
}