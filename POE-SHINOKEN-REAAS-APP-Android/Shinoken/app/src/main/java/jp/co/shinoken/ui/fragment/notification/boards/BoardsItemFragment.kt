package jp.co.shinoken.ui.fragment.notification.boards

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
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentNotificationsItemBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.ui.EndlessScrollListener
import jp.co.shinoken.ui.fragment.notification.NotificationsFragmentDirections
import jp.co.shinoken.ui.fragment.notification.boards.item.BoardItem
import jp.co.shinoken.ui.fragment.notification.notifications.NotificationsItemFragment
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BoardsItemFragment : Fragment() {

    private val viewModel: BoardsItemViewModel by viewModels()
    private lateinit var binding: FragmentNotificationsItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentNotificationsItemBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = GroupAdapter<GroupieViewHolder>()

        lifecycleScope.launchWhenCreated {
            viewModel.boards.collect { boards ->
                adapter.update(boards.map { board ->
                    BoardItem(board) {
                        findNavController().navigate(
                            NotificationsFragmentDirections.actionNotificationsFragmentToBoardDetailFragment(
                                it
                            )
                        )
                    }
                })
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

        lifecycleScope.launchWhenCreated {
            viewModel.isLoading.collect {
                binding.recycler.isVisible = it.not()
                if (it) {
                    binding.notificationProgressBar.show()
                } else {
                    binding.notificationProgressBar.hide()
                    val isEmpty = viewModel.boards.value.isEmpty()
                    binding.emptyView.apply {
                        isVisible = isEmpty
                        setEmptyText(getString(R.string.boards_empty))
                    }
                    binding.recycler.isGone = isEmpty
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isNextLoading.collect {
                binding.notificationNextLoadingProgressBar.isVisible = it
                if (it) {
                    binding.notificationNextLoadingProgressBar.show()
                } else {
                    binding.notificationNextLoadingProgressBar.hide()
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.appError.collect {
                it ?: return@collect
                appError(it) {viewModel.fetch()}
            }
        }
    }

    companion object {
        fun newInstance() = NotificationsItemFragment()
    }
}