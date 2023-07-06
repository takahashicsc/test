package jp.co.shinoken.ui.fragment.coupons.item

import android.view.View
import coil.load
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemCouponsBinding
import jp.co.shinoken.model.api.Benefit

class CouponsItem(
    private val benefit: Benefit,
    private val listener: (String) -> Unit
) : BindableItem<ItemCouponsBinding>() {
    override fun bind(viewBinding: ItemCouponsBinding, position: Int) {
        viewBinding.cardLayout.setOnClickListener {
            listener.invoke(benefit.url)
        }
        viewBinding.couponTitle.text = benefit.title


        viewBinding.couponImg.load(benefit.images?.first()?.publishUrl)
    }

    override fun getLayout(): Int = R.layout.item_coupons

    override fun initializeViewBinding(view: View): ItemCouponsBinding {
        return ItemCouponsBinding.bind(view)
    }
}