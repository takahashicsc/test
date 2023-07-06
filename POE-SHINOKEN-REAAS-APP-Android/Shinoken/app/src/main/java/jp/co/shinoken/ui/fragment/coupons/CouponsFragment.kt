package jp.co.shinoken.ui.fragment.coupons

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentCouponsBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.extension.showChromeCustomTabs
import jp.co.shinoken.ui.EndlessScrollListener
import jp.co.shinoken.ui.fragment.coupons.item.CouponsItem
import jp.co.shinoken.ui.fragment.notification.NotificationsFragmentDirections
import jp.co.shinoken.ui.fragment.notification.notifications.item.NotificationItem
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CouponsFragment : Fragment() {
    private lateinit var binding: FragmentCouponsBinding
    private val viewModel: CouponsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCouponsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel
        val adapter = GroupAdapter<GroupieViewHolder>()

        lifecycleScope.launchWhenCreated {
            viewModel.benefits.collect { benefits ->
                adapter.update(benefits.map { benefit ->
                    CouponsItem(benefit = benefit) {
                        showChromeCustomTabs(it.toUri())
                    }
                })
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isLoading.collect {
                binding.recycler.isVisible = it.not()
                if (it) {
                    binding.benefitsProgressBar.show()
                } else {
                    binding.benefitsProgressBar.hide()
                    val isEmpty = viewModel.benefits.value.isEmpty()
                    binding.emptyView.apply {
                        isVisible = isEmpty
                        setEmptyText(getString(R.string.notifications_empty))
                    }
                    binding.recycler.isGone = isEmpty
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isNextLoading.collect {
                binding.benefitNextLoadingProgressBar.isVisible = it
                if (it) {
                    binding.benefitNextLoadingProgressBar.show()
                } else {
                    binding.benefitNextLoadingProgressBar.hide()
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.appError.collect {
                it ?: return@collect
                appError(it) { viewModel.fetch() }
            }
        }

        binding.recycler.apply {
            this.adapter = adapter
            this.layoutManager =
                LinearLayoutManager(requireContext())
            addOnScrollListener(
                object : EndlessScrollListener(layoutManager as LinearLayoutManager) {
                    override fun onLoadMore(currentPage: Int) {
                        viewModel.nextPageLoad()
                    }
                }
            )
        }
    }

    companion object {
        fun newInstance() = CouponsFragment()
    }

}