package jp.co.shinoken.ui.fragment.faq

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
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentFaqsBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.ui.fragment.faq.item.FaqCategoryItem
import jp.co.shinoken.ui.fragment.faq.item.FaqContentItem
import jp.co.shinoken.ui.fragment.home.HomeFragmentDirections
import jp.co.shinoken.ui.fragment.home.item.HomeSuggestionItem
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FaqsFragment : Fragment() {

    private lateinit var binding: FragmentFaqsBinding
    private val viewModel: FaqsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentFaqsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val categoryAdapter = GroupAdapter<GroupieViewHolder>()
        binding.categoriesRecycler.apply {
            this.adapter = categoryAdapter
            this.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }

        val suggestionAdapter = GroupAdapter<GroupieViewHolder>()
        binding.homeMenuHeader.headerRecycler.apply {
            this.adapter = suggestionAdapter
            val manager = LinearLayoutManager(requireContext())
            manager.orientation = LinearLayoutManager.HORIZONTAL
            this.layoutManager = manager
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isLoading.collect {
                if (it) {
                    binding.faqsProgressBar.show()
                } else {
                    binding.faqsProgressBar.hide()
                }

                binding.faqsView.isGone = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.faqs.collect { it ->
                val faqs = it ?: return@collect
                categoryAdapter.update(faqs.categories.map { category ->
                    FaqCategoryItem(faqCategoryItem = category) {
                        viewModel.setSelectCategory(it)
                    }
                })

                binding.categoriesRecycler.apply {
                    isVisible = faqs.categories.isNotEmpty()
                }
                binding.emptyView.isVisible = it.categories.isEmpty()
            }
        }

        val selectFaqContentsAdapter = GroupAdapter<GroupieViewHolder>()
        binding.faqListRecycler.apply {
            this.adapter = selectFaqContentsAdapter
            this.layoutManager =
                LinearLayoutManager(requireContext())
        }

        lifecycleScope.launchWhenCreated {
            viewModel.selectFaqContents.collect { faqContents ->
                selectFaqContentsAdapter.update(faqContents.map { faqContent ->
                    FaqContentItem(faqContent) {
                        findNavController().navigate(
                            FaqsFragmentDirections.actionFaqFragmentToFaqDetailFragment(it)
                        )
                    }
                })

                binding.faqListRecycler.apply {
                    isGone = faqContents.isNullOrEmpty()
                }

                binding.emptyView.apply {
                    isVisible = faqContents.isNullOrEmpty()
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.suggestions.collect { suggestions->
                (binding.homeMenuHeader.headerRecycler.adapter as GroupAdapter).update(suggestions.map { suggestion ->
                    HomeSuggestionItem(suggestion) {
                        findNavController().navigate(
                            FaqsFragmentDirections.actionFaqFragmentToFaqDetailFragment(
                                it.id
                            )
                        )
                    }
                })
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.appError.collect {
                it ?: return@collect
                appError(it) { viewModel.fetch() }
            }
        }

        binding.emptyView.setEmptyText(getString(R.string.empty_text_format).format(getString(R.string.faq_title)))
    }

    companion object {
        fun newInstance() = FaqsFragment()
    }
}