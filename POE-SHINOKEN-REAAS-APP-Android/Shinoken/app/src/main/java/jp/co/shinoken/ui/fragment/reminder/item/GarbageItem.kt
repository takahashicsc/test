package jp.co.shinoken.ui.fragment.reminder.item

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemReminderBinding
import jp.co.shinoken.model.api.Reminder

class GarbageItem(
    private val items: List<Reminder>,
    private val listener: () -> Unit
) : BindableItem<ItemReminderBinding>() {
    override fun bind(viewBinding: ItemReminderBinding, position: Int) {
        viewBinding.childRecycler

        val adapter = GroupAdapter<GroupieViewHolder>()

        items.forEach { item ->
            adapter.add(GarbageChildItem(reminder = item) {
                listener.invoke()
            })
        }

        viewBinding.childRecycler.apply {
            this.adapter = adapter
            this.layoutManager =
                LinearLayoutManager(context)
        }
    }

    override fun getLayout(): Int = R.layout.item_reminder

    override fun initializeViewBinding(view: View): ItemReminderBinding {
        return ItemReminderBinding.bind(view)
    }
}