package jp.co.shinoken.ui.fragment.notification.detail

import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.databinding.FragmentNotificationDetailBinding
import jp.co.shinoken.extension.actionTelPhone
import jp.co.shinoken.extension.appError
import jp.co.shinoken.extension.convertHtmlString
import jp.co.shinoken.extension.showBrowser
import jp.co.shinoken.model.api.Kind
import jp.co.shinoken.model.api.Link
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class NotificationDetailFragment : Fragment() {
    private val viewModel: NotificationDetailViewModel by viewModels()
    private lateinit var binding: FragmentNotificationDetailBinding
    private lateinit var listener: INotificationDetailFragment

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
            viewModel.notificationDetail.collect { notification ->
                notification ?: return@collect
                binding.notificationTitle.text = notification.title
                binding.notificationDescription.loadData(
                    notification.contentText.convertHtmlString(),
                    "text/html",
                    "UTF8"
                )
                binding.notificationLinkItemLayout.apply {
                    isVisible = notification.links.isNotEmpty()
                }
                binding.notificationLinkItem.apply {
                    setLinkItemText(notification.title)
                    setOnClickListener {
                        navigate(notification.links.first())
                    }
                }
                binding.notificationDate.apply {
                    isVisible = notification.segmentedAt != null
                    text = notification.getSegmentedAtFormatString()
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

    private fun navigate(link: Link) {
        when (link.kind) {
            Kind.Link -> showBrowser(link.url.toUri())
            Kind.Tel -> actionTelPhone(link.url)
            Kind.DeepLink -> listener.navigateScreen(link)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is INotificationDetailFragment) {
            listener = context
        }
    }

    interface INotificationDetailFragment {
        fun navigateScreen(faqLink: Link)
    }

    companion object {
        fun newInstance() = NotificationDetailFragment()
    }
}