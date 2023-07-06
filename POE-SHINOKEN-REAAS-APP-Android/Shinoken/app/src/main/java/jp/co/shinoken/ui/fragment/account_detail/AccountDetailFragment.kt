package jp.co.shinoken.ui.fragment.account_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentAccountDetailBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.model.BirthDay
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AccountDetailFragment : Fragment() {

    private val viewModel: AccountDetailViewModel by viewModels()

    private lateinit var binding: FragmentAccountDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAccountDetailBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.isLoading.collect {
                if (it) {
                    binding.accountDetailProgressBar.show()
                } else {
                    binding.accountDetailProgressBar.hide()
                }

                binding.accountDetailProgressBar.isGone = it.not()
                binding.accountDetailView.isVisible = it.not()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.contract.collect {
                it ?: return@collect

                binding.contractStatus.text =
                    getString(if (it.active) R.string.account_contract_active else R.string.account_contract_inactive)
                binding.contractName.text = it.residenceName

                BirthDay.parseBirthDay(it.residentBirthDay)?.let { birthday ->
                    binding.birthdayText.text =
                        birthday.getBirthDayFormatString(getString(R.string.birth_day_format))
                }
                binding.nameText.text = it.residentName

                binding.nameKanaText.text = it.residentNameKana
                binding.phoneNumberText.text = it.residentTel
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.appError.collect {
                it ?: return@collect
                appError(it) { viewModel.fetch() }
            }
        }
    }

    companion object {
        fun newInstance() = AccountDetailFragment()
    }
}