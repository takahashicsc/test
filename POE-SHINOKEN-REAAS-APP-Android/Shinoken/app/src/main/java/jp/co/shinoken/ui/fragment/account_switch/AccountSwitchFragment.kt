package jp.co.shinoken.ui.fragment.account_switch

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.databinding.FragmentAccountSwitchBinding
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.model.api.DetectedAccountable
import jp.co.shinoken.ui.fragment.account_switch.item.AccountSwitchItem
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AccountSwitchFragment : Fragment() {

    companion object {
        fun newInstance() = AccountSwitchFragment()
    }

    private val viewModel: AccountSwitchViewModel by viewModels()
    private lateinit var binding: FragmentAccountSwitchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAccountSwitchBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.apiState.collect {
                binding.accountSwitchProgressBar.hide()
                when (it) {
                    ApiState.Empty -> return@collect
                    ApiState.Success -> {
                        findNavController().navigate(
                            AccountSwitchFragmentDirections.actionAccountSwitchFragmentToHomeFragment(
                                true
                            )
                        )
                    }
                    ApiState.LOADING -> {
                        binding.accountSwitchProgressBar.show()
                        binding.emptyView.isGone = true
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.userInfo.collect {
                it ?: return@collect

                binding.emptyView.isVisible = it.data.detectedAccountables.isEmpty()

                val adapter = GroupAdapter<GroupieViewHolder>()

                binding.recycler.apply {
                    isVisible = it.data.detectedAccountables.isNotEmpty()
                    this.adapter = adapter
                    this.layoutManager = LinearLayoutManager(requireContext())
                }

                adapterUpdate(it.data.detectedAccountables)
            }
        }
    }

    private fun adapterUpdate(detectedAccountables: List<DetectedAccountable>) {
        (binding.recycler.adapter as GroupAdapter).update(
            detectedAccountables.map { detectedAccountable ->
                AccountSwitchItem(
                    detectedAccountable,
                    viewModel.isCurrentAccount(detectedAccountable.authId)
                ) { authId ->
                    viewModel.accountSwitch(authId)
                }
            }
        )
    }

}