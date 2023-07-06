package jp.co.shinoken.ui.fragment.notification.detail

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
import jp.co.shinoken.databinding.FragmentNotificationDetailBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.extension.convertHtmlString
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class BoardDetailFragment : Fragment() {
    private val viewModel: BoardDetailViewModel by viewModels()
    private lateinit var binding: FragmentNotificationDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentNotificationDetailBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.isLoading.collect {
                if (it) {
                    binding.notificationProgressBar.show()
                } else {
                    binding.notificationProgressBar.hide()
                }

                binding.notificationView.isGone = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.boardDetail.collect {
                it ?: return@collect
                binding.notificationTitle.text = it.title
                it.contentText?.let { contentText ->
                    binding.notificationDescription.loadData(
                        contentText.convertHtmlString(),
                        "text/html",
                        "UTF8"
                    )
                }
                binding.notificationLinkItemLayout.isVisible = false
                binding.notificationDate.apply {
                    isVisible = it.startAt != null
                    text = it.getStartAtFormatString()
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

    companion object {
        fun newInstance() = NotificationDetailFragment()
    }
}