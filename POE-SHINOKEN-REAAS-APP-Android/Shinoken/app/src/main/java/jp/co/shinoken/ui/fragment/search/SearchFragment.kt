package jp.co.shinoken.ui.fragment.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.databinding.FragmentSearchBinding
import jp.co.shinoken.ui.fragment.search.item.SearchResultItem
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var binding: FragmentSearchBinding

    private lateinit var listener: ISearchFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSearchBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.deleteButton.setOnClickListener {
            binding.searchEditText.text = null
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                text: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.search(searchText = text.toString())
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        val adapter = GroupAdapter<GroupieViewHolder>()

        binding.recycler.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(requireContext())
        }

        lifecycleScope.launchWhenStarted {
            viewModel.searchResult.collect { searchResult ->
                searchResult.tags

                adapter.update(searchResult.tags.map { tag ->
                    SearchResultItem(tag) { fragmentId ->
                        listener.navigate(fragmentId)
                    }
                })
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ISearchFragment) {
            listener = context
        }
    }

    interface ISearchFragment {
        fun navigate(fragmentId: Int)
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}