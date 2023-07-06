package jp.co.shinoken.ui.fragment.cohabitant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentCohabitantDetailBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.extension.showAlertDialog
import jp.co.shinoken.extension.showSnackBar
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.model.BirthDay
import jp.co.shinoken.model.ResideType
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CohabitantDetailFragment : Fragment() {

    private val viewModel: CohabitantDetailViewModel by viewModels()
    private lateinit var binding: FragmentCohabitantDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCohabitantDetailBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.cohabitant.collect {
                it ?: return@collect
                (requireActivity() as AppCompatActivity).supportActionBar?.title = it.name
                binding.nameText.text = it.name
                binding.emailText.apply {
                    isVisible = it.email.isNullOrEmpty().not()
                    text = it.email
                }
                binding.birthdayText.apply {
                    isVisible = it.birthday.isNullOrEmpty().not()
                    text = BirthDay.parseBirthDay(it.birthday)
                        ?.getBirthDayFormatString(getString(R.string.birth_day_format))
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.resideType.collect {
                it ?: return@collect
                binding.cohabitantRequestButton.isVisible = it == ResideType.Cohabitant
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isShowChargeButton.collect {
                binding.showChargeLayout.isVisible = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.apiState.collect {
                binding.cohabitantDetailProgressBar.hide()
                binding.cohabitantRequestButton.isEnabled = it == ApiState.Empty
                when (it) {
                    ApiState.Empty -> return@collect
                    ApiState.Success -> {
                        binding.cohabitantRequestButton.isEnabled = false
                        showSnackBar(
                            view = binding.cohabitantRequestButton,
                            message = getString(R.string.cohabitant_delete_success)
                        )
                    }
                    ApiState.LOADING -> {
                        binding.cohabitantDetailProgressBar.show()
                    }
                }
            }
        }


        lifecycleScope.launchWhenCreated {
            viewModel.appError.collect {
                it ?: return@collect
                appError(it) { viewModel.fetch() }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.deleteApiError.collect {
                it ?: return@collect
                appError(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isShowCharge.collect {
                binding.showChargeCheck.isChecked = it
            }
        }

        binding.cohabitantRequestButton.setOnClickListener {
            showAlertDialog(
                title = getString(R.string.cohabitant_delete_dialog_title),
                message = getString(R.string.cohabitant_delete_dialog_message),
                positiveText = "OK",
                positiveListener = {
                    viewModel.deleteCohabitant()
                },
                negativeText = getString(R.string.cancel)
            )
        }

        binding.showChargeCheck.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.patchSubresidentBillingActive()
            } else {
                viewModel.patchSubresidentBillingInActive()
            }
        }
    }

    companion object {
        fun newInstance() = CohabitantDetailFragment()
    }

}