package jp.co.shinoken.ui.fragment.cohabitant.item

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemCohabitantsBinding
import jp.co.shinoken.model.Cohabitant
import jp.co.shinoken.model.CohabitantParent
import jp.co.shinoken.model.ResideType

class CohabitantsItem(
    private val items: CohabitantParent,
    private val listener: (Cohabitant, ResideType) -> Unit
) : BindableItem<ItemCohabitantsBinding>() {
    override fun bind(viewBinding: ItemCohabitantsBinding, position: Int) {
        viewBinding.childRecycler

        val adapter = GroupAdapter<GroupieViewHolder>()

        viewBinding.parentText.apply {
            text = this.context.getString(items.resideType.nameRes)
        }

        items.cohabitants.forEach { item ->
            adapter.add(CohabitantsChildItem(item) {
                listener.invoke(it, items.resideType)
            })
        }

        viewBinding.childRecycler.apply {
            this.adapter = adapter
            this.layoutManager =
                LinearLayoutManager(context)
        }
    }

    override fun getLayout(): Int = R.layout.item_cohabitants

    override fun initializeViewBinding(view: View): ItemCohabitantsBinding {
        return ItemCohabitantsBinding.bind(view)
    }
}