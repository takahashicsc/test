package jp.co.shinoken.ui.fragment.manual

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajat.pdfviewer.PdfViewerActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.databinding.FragmentManualBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.ui.fragment.manual.item.ManualsItem
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ManualFragment : Fragment() {

    private val viewModel: ManualViewModel by viewModels()
    private lateinit var binding: FragmentManualBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentManualBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupAdapter<GroupieViewHolder>()
        binding.recycler.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(requireContext())
        }
        lifecycleScope.launchWhenCreated {
            viewModel.manuals.collect {
                it ?: return@collect

                binding.emptyView.isVisible = it.data.isEmpty()
                binding.recycler.isVisible = it.data.isNotEmpty()

                adapter.update(it.data.map { manual ->
                    ManualsItem(manual) { urlString ->
                        startActivity(
                            PdfViewerActivity.launchPdfFromUrl(
                                requireActivity(), urlString,
                                manual.title, "dir", true
                            )
                        )
                    }
                })
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isLoading.collect {
                if (it) {
                    binding.manualProgressBar.show()
                } else {
                    binding.manualProgressBar.hide()
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
        fun newInstance() = ManualFragment()
    }
}