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
import jp.co.shinoken.databinding.FragmentSelectWeekBinding
import jp.co.shinoken.ui.fragment.reminder.item.SelectWeekItem

@AndroidEntryPoint
class SelectWeekFragment : Fragment() {

    private val viewModel: SelectWeekViewModel by viewModels()
    private lateinit var binding: FragmentSelectWeekBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSelectWeekBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupAdapter<GroupieViewHolder>()
        val items = listOf("月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日", "日曜日")
        adapter.add(SelectWeekItem(items = items) {
            findNavController().navigate(SelectWeekFragmentDirections.actionSelectWeekFragmentToSelectDayOfWeekFragment())
        })

        binding.recycler.apply {
            this.adapter = adapter
            this.layoutManager =
                LinearLayoutManager(context)
        }
    }

    companion object {
        fun newInstance() = SelectWeekFragment()
    }
}