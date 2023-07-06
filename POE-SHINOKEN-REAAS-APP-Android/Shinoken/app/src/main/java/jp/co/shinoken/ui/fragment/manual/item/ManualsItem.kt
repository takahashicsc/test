package jp.co.shinoken.ui.fragment.manual.item

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemManualsBinding
import jp.co.shinoken.model.api.Manual

class ManualsItem(
    private val manual: Manual,
    private val listener: (String) -> Unit
) : BindableItem<ItemManualsBinding>() {
    override fun bind(viewBinding: ItemManualsBinding, position: Int) {
        viewBinding.manual.setOnClickListener {
            listener.invoke(manual.asset.url)
        }
        viewBinding.manual.setLinkItemText(manual.title)
    }

    override fun getLayout(): Int = R.layout.item_manuals

    override fun initializeViewBinding(view: View): ItemManualsBinding {
        return ItemManualsBinding.bind(view)
    }
}