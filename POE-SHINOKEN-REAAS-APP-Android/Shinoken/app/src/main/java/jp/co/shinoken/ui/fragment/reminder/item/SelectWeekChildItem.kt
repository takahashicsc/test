package jp.co.shinoken.ui.fragment.reminder.item

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemSelectWeekChildBinding

class SelectWeekChildItem(
    private val string: String,
    private val listener: () -> Unit
) : BindableItem<ItemSelectWeekChildBinding>() {
    override fun bind(viewBinding: ItemSelectWeekChildBinding, position: Int) {
        viewBinding.dayOfWeekView.apply {
            setLinkItemText(string)
            setOnClickListener {
                listener.invoke()
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_select_week_child

    override fun initializeViewBinding(view: View): ItemSelectWeekChildBinding {
        return ItemSelectWeekChildBinding.bind(view)
    }
}