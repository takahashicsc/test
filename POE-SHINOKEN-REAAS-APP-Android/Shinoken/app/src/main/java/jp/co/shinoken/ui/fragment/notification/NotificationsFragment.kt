package jp.co.shinoken.ui.fragment.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentNotificationsBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.ui.fragment.notification.boards.BoardsItemFragment
import jp.co.shinoken.ui.fragment.notification.notifications.NotificationsItemFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private val viewModel: NotificationsViewModel by viewModels()
    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentNotificationsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pager.adapter = NotificationsPagerAdapter(requireActivity())
        TabLayoutMediator(
            binding.tabLayout,
            binding.pager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.apply {
                setCustomView(R.layout.item_tab_title)
                this.customView?.findViewById<TextView>(R.id.tab_text)?.text =
                    getString(if (position == 0) R.string.notifications_board else R.string.notifications_notification)

                this.customView?.findViewById<ImageView>(R.id.tab_badge)?.isVisible = false
            }
        }.attach()

        lifecycleScope.launchWhenCreated {
            viewModel.unReadCount.collect { unReadCount ->
                if (unReadCount == 0) return@collect
                TabLayoutMediator(
                    binding.tabLayout,
                    binding.pager
                ) { tab: TabLayout.Tab, position: Int ->
                    tab.apply {
                        setCustomView(R.layout.item_tab_title)
                        this.customView?.findViewById<TextView>(R.id.tab_text)?.text =
                            getString(if (position == 0) R.string.notifications_board else R.string.notifications_notification)

                        this.customView?.findViewById<ImageView>(R.id.tab_badge)?.isVisible =
                            NotificationsPosition == position && unReadCount != 0
                    }
                }.attach()
            }
        }
    }

    companion object {
        const val NotificationsPosition = 1
        const val BoardsPosition = 0
        fun newInstance() = NotificationsFragment()
    }

    private inner class NotificationsPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment =
            when (position) {
                BoardsPosition -> BoardsItemFragment()
                NotificationsPosition -> NotificationsItemFragment()
                else -> NotificationsItemFragment()
            }
    }
}