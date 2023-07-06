package jp.co.shinoken.ui.fragment.reminder.item

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemCollectionDateTypeFormChildBinding

class CollectionDateTypeFormChildItem(
    private val string: String,
    private val listener: () -> Unit
) : BindableItem<ItemCollectionDateTypeFormChildBinding>() {
    override fun bind(viewBinding: ItemCollectionDateTypeFormChildBinding, position: Int) {
        viewBinding.itemView.apply {
            setLinkItemText(string)
            setOnClickListener { listener.invoke() }
        }
    }

    override fun getLayout(): Int = R.layout.item_collection_date_type_form_child

    override fun initializeViewBinding(view: View): ItemCollectionDateTypeFormChildBinding {
        return ItemCollectionDateTypeFormChildBinding.bind(view)
    }
}