package jp.co.shinoken.ui.fragment.charges.item

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemChargeDetailBinding
import jp.co.shinoken.model.Charge
import jp.co.shinoken.model.ChargesType

class ChargeDetailItem(
    private val charge: Charge,
    private val listener: () -> Unit
) : BindableItem<ItemChargeDetailBinding>() {
    override fun bind(viewBinding: ItemChargeDetailBinding, position: Int) {
        viewBinding.chargeType.apply {
            text = charge.name
        }

        viewBinding.charge.apply {
            text = this.context.getString(R.string.bill_detail_fee_format).format(charge.charge)
        }
    }

    override fun getLayout(): Int = R.layout.item_charge_detail

    override fun initializeViewBinding(view: View): ItemChargeDetailBinding {
        return ItemChargeDetailBinding.bind(view)
    }
}