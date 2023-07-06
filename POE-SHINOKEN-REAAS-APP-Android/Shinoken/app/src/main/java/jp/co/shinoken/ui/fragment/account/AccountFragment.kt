package jp.co.shinoken.ui.fragment.account

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentAccountBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.extension.convertUriToFile
import jp.co.shinoken.extension.showChromeCustomTabs
import jp.co.shinoken.model.api.Me
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private val viewModel: AccountViewModel by viewModels()
    private lateinit var binding: FragmentAccountBinding

    private lateinit var listener: IAccountFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAccountBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetch()

        binding.checkForm.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToCheckFormFragment())
        }

        binding.lifeLine.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToLifelineContactsFragment())
        }

        binding.profileImg.setOnClickListener {
            listener.selectImage()
        }

        lifecycleScope.launchWhenCreated {
            viewModel.iconImageUri.collect {
                it ?: return@collect
                binding.profileImg.load("$it?${Date()}") {
                    memoryCachePolicy(CachePolicy.DISABLED)
                    transformations(CircleCropTransformation())
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isIconReplace.collect {
                if (it.not()) return@collect
                binding.profileImg.load("${viewModel.iconImageUri.value}?${Date()}") {
                    memoryCachePolicy(CachePolicy.DISABLED)
                    transformations(CircleCropTransformation())
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isLoading.collect {
                if (it) {
                    binding.accountProgressBar.show()
                } else {
                    binding.accountProgressBar.hide()
                }

                binding.accountProgressBar.isGone = it.not()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.me.collect { me ->
                me ?: return@collect

                binding.accountName.text = getString(R.string.account_name_format).format(me.name)
                binding.buildingName.text =
                    getString(R.string.account_building_name_format).format(
                        me.building.name,
                        me.room.number
                    )
                binding.checkForm.apply {
                    setLinkItemDescription(
                        if (me.checkForm.status == Me.CheckForm.Status.Draft) {
                            getString(R.string.account_check_form_status_draft)
                        } else {
                            getString(R.string.account_check_form_status_submitted)
                        }
                    )
                }
                binding.mailTransfer.setOnClickListener {
                    showChromeCustomTabs(me.meta.mailTransfer.toUri())
                }

                binding.cancelForm.apply {
                    isVisible = me.accountableType == Me.AccountableType.Resident
                    setOnClickListener {
                        showChromeCustomTabs(me.meta.cancelRequest.toUri())
                    }
                }

                binding.accountCohabitant.apply {
                    setLinkItemDescription(
                        getString(R.string.number_of_people_format).format(
                            me.membersCount
                        )
                    )
                    setOnClickListener {
                        findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToCohabitantsFragment())
                    }
                }


                binding.insurance.apply {
                    // Phase1では表示できないため非表示
                    isVisible = false
                    //isVisible = me.insurance != null && me.accountableType == Me.AccountableType.Resident
                    if (me.insurance == null) return@apply
                    setOnClickListener {
                        findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToInsuranceFragment())
                    }
                    binding.insurance.setLinkItemDescription(getString(if (me.insurance.active) R.string.account_contract_active else R.string.account_contract_inactive))
                }

                binding.accountContract.apply {
                    isVisible = me.accountableType == Me.AccountableType.Resident
                    setOnClickListener {
                        findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToAccountDetailFragment())
                    }
                    setLinkItemDescription(
                        if (me.contract.active) {
                            if (me.contract.daysToEndOfContract <= 90) {
                                getString(R.string.account_contract_renewal_format).format(me.contract.daysToEndOfContract)
                            } else {
                                getString(R.string.account_contract_active)
                            }
                        } else {
                            getString(R.string.account_contract_inactive)
                        }
                    )
                }

            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.appError.collect {
                it ?: return@collect
                appError(it) { viewModel.fetch() }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IAccountFragment) {
            listener = context
        }
    }

    fun setProfileImage(uri: Uri) {
        lifecycleScope.launch {
            val file = convertUriToFile(uri)
            val setFormImage = async { viewModel.setIconImageUri(file) }
            setFormImage.await()
        }
    }

    companion object {
        fun newInstance() = AccountFragment()
    }

    interface IAccountFragment {
        fun selectImage()
    }
}