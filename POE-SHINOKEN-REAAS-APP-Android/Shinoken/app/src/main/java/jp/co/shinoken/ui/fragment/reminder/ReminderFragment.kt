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
import jp.co.shinoken.databinding.FragmentReminderBinding
import jp.co.shinoken.model.api.Reminder
import jp.co.shinoken.model.Weekday
import jp.co.shinoken.ui.fragment.reminder.item.GarbageItem

@AndroidEntryPoint
class ReminderFragment : Fragment() {
    private val viewModel: ReminderViewModel by viewModels()
    private lateinit var binding: FragmentReminderBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentReminderBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupAdapter<GroupieViewHolder>()
        val items = listOf(
            Reminder(
                before = Reminder.Before.Day0,
                hour = 21,
                minute = 0,
                name = "可燃ごみ",
                mode = Reminder.Mode.Weekly(listOf(Weekday.Sun))
            ),
            Reminder(
                before = Reminder.Before.Day1,
                hour = 8,
                minute = 0,
                name = "瓶・缶",
                mode = Reminder.Mode.Custom(week = 2, listOf(Weekday.Wed))
            ),
            Reminder(
                before = Reminder.Before.Day1,
                hour = 8,
                minute = 0,
                name = "ペットボトル",
                mode = Reminder.Mode.Monthly(day = 1)
            )
        )
        adapter.add(GarbageItem(items = items) {

        })

        binding.recycler.apply {
            this.adapter = adapter
            this.layoutManager =
                LinearLayoutManager(context)
        }

        binding.garbageAddButton.setOnClickListener {
            findNavController().navigate(ReminderFragmentDirections.actionReminderFragmentToGarbageDetailFragment())
        }
    }

    companion object {
        fun newInstance() = ReminderFragment()
    }
}