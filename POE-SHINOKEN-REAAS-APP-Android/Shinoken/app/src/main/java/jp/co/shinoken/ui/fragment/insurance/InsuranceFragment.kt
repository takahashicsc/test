package jp.co.shinoken.ui.fragment.insurance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentInsuranceBinding
import jp.co.shinoken.extension.appError
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class InsuranceFragment : Fragment() {

    private val viewModel: InsuranceViewModel by viewModels()
    private lateinit var binding: FragmentInsuranceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentInsuranceBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Phase1では更新ができないため使用しない
//        binding.insuranceLinkLayout.setOnClickListener {
//            findNavController().navigate(InsuranceFragmentDirections.actionInsuranceFragmentToInsuranceFormFragment())
//        }

        lifecycleScope.launchWhenCreated {
            viewModel.isLoading.collect {
                if (it) {
                    binding.insuranceProgressBar.show()
                } else {
                    binding.insuranceProgressBar.hide()
                }

                binding.insuranceProgressBar.isGone = it.not()
                binding.insuranceView.isVisible = it.not()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.insurance.collect {
                it ?: return@collect

                binding.insurancePeriod.text = getString(R.string.period_format).format(
                    it.getStartedAtFormatString(),
                    it.getEndAtFormatString()
                )

                binding.insuranceCompany.text = it.company

                binding.insurancePlan.text = it.plan
                binding.insurancePlanLayout.isVisible = it.plan.isNullOrEmpty().not()
                binding.insuranceStatus.text =
                    getString(if (it.active) R.string.account_contract_active else R.string.account_contract_inactive)
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
        fun newInstance() = InsuranceFragment()
    }
}