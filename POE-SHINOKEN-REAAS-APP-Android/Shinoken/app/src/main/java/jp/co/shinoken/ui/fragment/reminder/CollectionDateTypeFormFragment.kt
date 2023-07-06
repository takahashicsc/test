package jp.co.shinoken.ui.fragment.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.databinding.FragmentCollectionDateTypeFormBinding
import jp.co.shinoken.ui.fragment.reminder.item.CollectionDateTypeFormItem

@AndroidEntryPoint
class CollectionDateTypeFormFragment : Fragment() {
    private val viewModel: CollectionDateTypeFormViewModel by viewModels()
    private lateinit var binding: FragmentCollectionDateTypeFormBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCollectionDateTypeFormBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupAdapter<GroupieViewHolder>()
        val items = listOf("毎週", "指定回目指定日", "毎月指定日", "指定日")
        adapter.add(CollectionDateTypeFormItem(items = items) {
            findNavController().navigate(CollectionDateTypeFormFragmentDirections.actionCollectionDateTypeFormFragmentToSelectWeekFragment())
        })

        binding.recycler.apply {
            this.adapter = adapter
            this.layoutManager =
                LinearLayoutManager(context)
        }
    }

    companion object {
        fun newInstance() = CollectionDateTypeFormFragment()
    }
}