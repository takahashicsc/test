package jp.co.shinoken.ui.fragment.reminder.item

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemSelectDayOfWeekChildBinding

class SelectDayOfWeekChildItem(
    private val string: String,
    private val listener: () -> Unit
) : BindableItem<ItemSelectDayOfWeekChildBinding>() {
    override fun bind(viewBinding: ItemSelectDayOfWeekChildBinding, position: Int) {
        viewBinding.weekText.text = string
        viewBinding.weekSelectLayout.setOnClickListener {
            viewBinding.weekSelectCheck.isChecked = viewBinding.weekSelectCheck.isChecked.not()
        }
    }

    override fun getLayout(): Int = R.layout.item_select_week_child

    override fun initializeViewBinding(view: View): ItemSelectDayOfWeekChildBinding {
        return ItemSelectDayOfWeekChildBinding.bind(view)
    }
}