package jp.co.shinoken.ui.fragment.topic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.databinding.FragmentTopicsBinding
import jp.co.shinoken.extension.showChromeCustomTabs
import jp.co.shinoken.ui.fragment.topic.item.TopicItem

@AndroidEntryPoint
class TopicsFragment : Fragment() {

    private val viewModel: TopicsViewModel by viewModels()
    private lateinit var binding: FragmentTopicsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentTopicsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupAdapter<GroupieViewHolder>()

        viewModel.topics.observe(viewLifecycleOwner, { topics ->
            topics ?: return@observe
            topics.forEach { topic ->
                adapter.add(TopicItem(topic) { uriString ->
                    showChromeCustomTabs(uriString.toUri())
                })
            }
        })

        binding.recycler.apply {
            this.adapter = adapter
            this.layoutManager =
                LinearLayoutManager(requireContext())
        }

        viewModel.fetch()
    }

    companion object {
        fun newInstance() = TopicsFragment()
    }
}