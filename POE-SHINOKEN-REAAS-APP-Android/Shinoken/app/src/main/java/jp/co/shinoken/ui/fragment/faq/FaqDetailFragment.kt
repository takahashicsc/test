package jp.co.shinoken.ui.fragment.faq

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.databinding.FragmentFaqDetailBinding
import jp.co.shinoken.extension.actionTelPhone
import jp.co.shinoken.extension.appError
import jp.co.shinoken.extension.convertHtmlString
import jp.co.shinoken.extension.showBrowser
import jp.co.shinoken.model.api.Kind
import jp.co.shinoken.model.api.Link
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FaqDetailFragment : Fragment() {

    private val viewModel: FaqDetailViewModel by viewModels()
    private lateinit var binding: FragmentFaqDetailBinding
    private lateinit var listener: IFaqDetailFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        return FragmentFaqDetailBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.isLoading.collect {
                if (it) {
                    binding.faqDetailProgressBar.show()
                } else {
                    binding.faqDetailProgressBar.hide()
                }

                binding.faqDetailView.isGone = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.faqDetail.collect {
                it ?: return@collect
                binding.telPhoneButton.apply {
                    isGone = it.links.isEmpty()
                    if (it.links.isNotEmpty()) {
                        val link = it.links.first()
                        text = link.title
                        setOnClickListener {
                            navigate(link)
                        }
                    }
                }
                binding.faqTitle.text = it.title
                binding.faqDescription.loadData(
                    it.contentText.convertHtmlString(),
                    "text/html",
                    "UTF8"
                )
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.appError.collect {
                it ?: return@collect
                appError(it){ viewModel.fetch() }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IFaqDetailFragment) {
            listener = context
        }
    }

    private fun navigate(link: Link) {
        when (link.kind) {
            Kind.Link -> showBrowser(link.url.toUri())
            Kind.Tel -> actionTelPhone(link.url)
            Kind.DeepLink -> listener.navigateScreen(link)
        }
    }

    interface IFaqDetailFragment {
        fun navigateScreen(faqLink: Link)
    }

    companion object {
        fun newInstance() = FaqDetailFragment()
    }
}