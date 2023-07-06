package jp.co.shinoken.ui.fragment.reminder.item

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemSelectWeekBinding

class SelectWeekItem(
    private val items: List<String>,
    private val listener: () -> Unit
) : BindableItem<ItemSelectWeekBinding>() {
    override fun bind(viewBinding: ItemSelectWeekBinding, position: Int) {
        viewBinding.childRecycler

        val adapter = GroupAdapter<GroupieViewHolder>()

        items.forEach { item ->
            adapter.add(SelectWeekChildItem(item) {
                listener.invoke()
            })
        }

        viewBinding.childRecycler.apply {
            this.adapter = adapter
            this.layoutManager =
                LinearLayoutManager(context)
        }
    }

    override fun getLayout(): Int = R.layout.item_select_week

    override fun initializeViewBinding(view: View): ItemSelectWeekBinding {
        return ItemSelectWeekBinding.bind(view)
    }
}