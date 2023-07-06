package jp.co.shinoken.ui.fragment.charges.item

import android.view.View
import androidx.core.view.isVisible
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.model.Charge
import jp.co.shinoken.databinding.ItemChargesBinding
import jp.co.shinoken.model.ChargesType

class ChargesItem(
    private val charge: Charge,
    private val listener: (String) -> Unit
) : BindableItem<ItemChargesBinding>() {
    override fun bind(viewBinding: ItemChargesBinding, position: Int) {
        if (charge.chargesType == ChargesType.Other) {
            viewBinding.chargeLayout.setOnClickListener {
                listener.invoke(charge.chargeDate)
            }
        }
        viewBinding.linkArrow.isVisible = charge.chargesType == ChargesType.Other

        viewBinding.charge.apply {
            text = this.context.getString(R.string.unit_format).format(charge.charge)
        }
        viewBinding.chargeDate.apply {
            val datePair = charge.getChargeDateFormatString() ?: return
            text = this.context.getString(R.string.charges_date_format)
                .format(datePair.first, datePair.second)
        }
    }

    override fun getLayout(): Int = R.layout.item_charges

    override fun initializeViewBinding(view: View): ItemChargesBinding {
        return ItemChargesBinding.bind(view)
    }
}