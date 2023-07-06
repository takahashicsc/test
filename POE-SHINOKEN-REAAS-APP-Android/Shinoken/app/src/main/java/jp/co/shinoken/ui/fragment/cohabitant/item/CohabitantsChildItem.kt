package jp.co.shinoken.ui.fragment.cohabitant.item

import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemCohabitantsChildBinding
import jp.co.shinoken.model.Cohabitant

class CohabitantsChildItem(
    private val item: Cohabitant,
    private val listener: (Cohabitant) -> Unit
) : BindableItem<ItemCohabitantsChildBinding>() {
    override fun bind(viewBinding: ItemCohabitantsChildBinding, position: Int) {
        viewBinding.cohabitantsChildLayout.setOnClickListener {
            listener.invoke(item)
        }

        viewBinding.cohabitantsName.text = item.name

        viewBinding.cohabitantsStatus.apply {
            background = ResourcesCompat.getDrawable(
                context.resources,
                item.status.backgroundRes,
                null
            )
            text = context.getString(item.status.textRes)
        }
    }

    override fun getLayout(): Int = R.layout.item_cohabitants_child

    override fun initializeViewBinding(view: View): ItemCohabitantsChildBinding {
        return ItemCohabitantsChildBinding.bind(view)
    }
}