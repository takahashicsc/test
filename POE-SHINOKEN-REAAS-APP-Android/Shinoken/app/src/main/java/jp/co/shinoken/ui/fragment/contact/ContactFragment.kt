package jp.co.shinoken.ui.fragment.contact

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentContactBinding
import jp.co.shinoken.extension.showChromeCustomTabs
import jp.co.shinoken.extension.showSnackBar
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.ui.fragment.contract_update.ContractUpdateFragment
import jp.co.shinoken.ui.fragment.home.HomeFragmentDirections
import jp.co.shinoken.ui.fragment.home.item.HomeSuggestionItem
import jp.co.shinoken.ui.fragment.warning_dialog.WarningDialogFragment
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ContactFragment : Fragment() {

    private val viewModel: ContactViewModel by viewModels()
    private lateinit var binding: FragmentContactBinding
    private var isShowDialog: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentContactBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val suggestionAdapter = GroupAdapter<GroupieViewHolder>()
        binding.homeMenuHeader.headerRecycler.apply {
            this.adapter = suggestionAdapter
            val manager = LinearLayoutManager(requireContext())
            manager.orientation = LinearLayoutManager.HORIZONTAL
            this.layoutManager = manager
        }

        lifecycleScope.launchWhenCreated {
            viewModel.categories.collect { categories ->
                binding.contactCategory.isGone = categories.isEmpty()
                if (categories.isEmpty()) return@collect
                val categoryStrings = categories.map { it.title }
                val adapter =
                    ArrayAdapter(
                        requireContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        categoryStrings
                    )
                binding.contactCategoryEdit.setAdapter(adapter)
                binding.contactCategoryEdit.setText(categoryStrings.first(), false)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.ages.collect {
                val agesStrings:List<String> = listOf("～19歳","20～29歳","30～39歳","40～49歳","50～59歳","60歳以上")
                val adapter =
                        ArrayAdapter(
                                requireContext(),
                                R.layout.support_simple_spinner_dropdown_item,
                                agesStrings
                        )
                binding.contactAgeEdit.setAdapter(adapter)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.prefectures.collect {
                val prefecturesStrings:List<String> = listOf("北海道","青森県","岩手県","宮城県","秋田県","山形県","福島県","茨城県","栃木県","群馬県","埼玉県","千葉県","東京都","神奈川県","新潟県","富山県","石川県","福井県","山梨県","長野県","岐阜県","静岡県","愛知県","三重県","滋賀県","京都府","大阪府","兵庫県","奈良県","和歌山県","鳥取県","島根県","岡山県","広島県","山口県","徳島県","香川県","愛媛県","高知県","福岡県","佐賀県","長崎県","熊本県","大分県","宮崎県","鹿児島県","沖縄県")
                val adapter =
                        ArrayAdapter(
                                requireContext(),
                                R.layout.support_simple_spinner_dropdown_item,
                                prefecturesStrings
                        )
                binding.contactAddressPrefecturesEdit.setAdapter(adapter)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.ages.collect {
                val professionsStrings:List<String> = listOf("会社員","公務員","自由業","パート","アルバイト","主婦","家事手伝い","学生","その他")
                val adapter =
                        ArrayAdapter(
                                requireContext(),
                                R.layout.support_simple_spinner_dropdown_item,
                                professionsStrings
                        )
                binding.contactProfessionEdit.setAdapter(adapter)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.suggestions.collect { suggestions->
                (binding.homeMenuHeader.headerRecycler.adapter as GroupAdapter).update(suggestions.map { suggestion ->
                    HomeSuggestionItem(suggestion) {
                        findNavController().navigate(
                            ContactFragmentDirections.actionContactFragmentToFaqDetailFragment(
                                it.id
                            )
                        )
                    }
                })
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isButtonEnable.collect {
                binding.contactSendButton.isEnabled = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.emailAddress.collect {
                binding.contactMailAddressEdit.setText(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.telnumber.collect {
                binding.contactTelNumberEdit.setText(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.name.collect {
                binding.contactNameEdit.setText(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.buildingName.collect {
                binding.contactBuildingNameEdit.setText(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.buildingAddress.collect {
                binding.contactBuildingAddressEdit.setText(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.roomNumber.collect {
                binding.contactRoomNumberEdit.setText(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.apiState.collect {
                binding.contactProgressBar.hide()
                viewModel.validation()
                when (it) {
                    ApiState.Empty -> return@collect
                    ApiState.Success -> {
                        showSnackBar(
                            view = binding.contactSendButton,
                            message = getString(R.string.contact_send_success)
                        )
                        findNavController().popBackStack()
                    }
                    ApiState.LOADING -> {
                        binding.contactProgressBar.show()
                        binding.contactSendButton.isEnabled = false
                    }
                }
            }
        }

        binding.contactSendButton.setOnClickListener {
            showWarningDialog()
        }

        binding.contactTelPhone.setOnClickListener {
            val telPhone = getString(R.string.sign_up_error_contact_phone_number)
            val telSchemeNumber = "tel:${telPhone}".toUri()
            startActivity(Intent(Intent.ACTION_DIAL, telSchemeNumber))
        }

        binding.contactCategoryEdit.addTextChangedListener(PasswordResetTextWatcher(binding.contactCategoryEdit))
        binding.contactMailAddressEdit.addTextChangedListener(PasswordResetTextWatcher(binding.contactMailAddressEdit))
        binding.contactTelNumberEdit.addTextChangedListener(PasswordResetTextWatcher(binding.contactTelNumberEdit))
        binding.contactNameEdit.addTextChangedListener(PasswordResetTextWatcher(binding.contactNameEdit))
        binding.contactContentEdit.addTextChangedListener(PasswordResetTextWatcher(binding.contactContentEdit))
        binding.contactBuildingNameEdit.addTextChangedListener(PasswordResetTextWatcher(binding.contactBuildingNameEdit))
        binding.contactBuildingAddressEdit.addTextChangedListener(PasswordResetTextWatcher(binding.contactBuildingAddressEdit))
        binding.contactRoomNumberEdit.addTextChangedListener(PasswordResetTextWatcher(binding.contactRoomNumberEdit))
        binding.contactAddressPrefecturesEdit.addTextChangedListener(PasswordResetTextWatcher(binding.contactAddressPrefecturesEdit))
        binding.contactAgeEdit.addTextChangedListener(PasswordResetTextWatcher(binding.contactAgeEdit))
        binding.contactProfessionEdit.addTextChangedListener(PasswordResetTextWatcher(binding.contactProfessionEdit))
        binding.contactRadioSex.setOnCheckedChangeListener{ _, checkedId->
            when(checkedId) {
                binding.contactRadioSexMale.id -> viewModel.selectSex = binding.contactRadioSexMale.text.toString()
                binding.contactRadioSexFemale.id -> viewModel.selectSex = binding.contactRadioSexFemale.text.toString()
            }
        }
    }
    private inner class PasswordResetTextWatcher(private val view: View) :
        TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            val text = charSequence.toString()
            when (view.id) {
                binding.contactCategoryEdit.id -> {
                    viewModel.selectCategory = text
                    binding.contactHelp.run {
                        setUnderLineText(
                            getString(R.string.contact_category_help_format).format(
                                text
                            )
                        )
                        setOnClickListener {
                            findNavController().navigate(
                                ContactFragmentDirections.actionContactFragmentToFaqFragment(
                                    categoryName = viewModel.categories.value.first { it.title == viewModel.selectCategory }.title
                                )
                            )
                        }
                    }
                }
                binding.contactMailAddressEdit.id -> viewModel.email = text
                binding.contactTelNumberEdit.id -> viewModel.tel = text
                binding.contactContentEdit.id -> viewModel.body = text
                binding.contactNameEdit.id -> viewModel.contactName = text
                binding.contactBuildingNameEdit.id -> viewModel.contactBuildingName = text
                binding.contactBuildingAddressEdit.id -> viewModel.contactBuildingAddress = text
                binding.contactRoomNumberEdit.id -> viewModel.contactRoomNumber = text
                binding.contactAddressPrefecturesEdit.id -> viewModel.selectPrefectures = text
                binding.contactAgeEdit.id -> viewModel.selectAge = text
                binding.contactProfessionEdit.id -> viewModel.selectProfession = text
            }
            viewModel.validation()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    private fun showWarningDialog() {
        WarningDialogFragment(
            title = getString(R.string.contact_caution_dialog_title),
            message = getString(R.string.contact_caution_dialog_message),
            buttonText = getString(R.string.contact_caution_dialog_button_text),
            underLineText = getString(R.string.contact_caution_dialog_under_line_text)
        ).apply {
            setButtonClickListener {
                viewModel.postInquiry()
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

    override fun onResume() {
        super.onResume()
        if (isShowDialog) {
            showWarningDialog()
            isShowDialog = false
        }
    }

    companion object {
        fun newInstance() = ContactFragment()
    }
}