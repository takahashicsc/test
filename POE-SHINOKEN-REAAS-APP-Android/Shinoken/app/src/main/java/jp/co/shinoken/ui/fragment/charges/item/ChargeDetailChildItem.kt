package jp.co.shinoken.ui.fragment.charges.item

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemChargeDetailChildBinding
import jp.co.shinoken.model.Charge

class ChargeDetailChildItem(
    private val charge: Charge,
    private val listener: () -> Unit
) : BindableItem<ItemChargeDetailChildBinding>() {
    override fun bind(viewBinding: ItemChargeDetailChildBinding, position: Int) {
    }

    override fun getLayout(): Int = R.layout.item_charge_detail_child

    override fun initializeViewBinding(view: View): ItemChargeDetailChildBinding {
        return ItemChargeDetailChildBinding.bind(view)
    }
}