package jp.co.shinoken.ui.fragment.account_switch.item

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemAccountSwitchBinding
import jp.co.shinoken.model.api.DetectedAccountable

class AccountSwitchItem(
    private val detectedAccountable: DetectedAccountable,
    private val isCurrentAccount: Boolean,
    private val listener: (String) -> Unit
) : BindableItem<ItemAccountSwitchBinding>() {
    private lateinit var viewBinding: ItemAccountSwitchBinding

    override fun bind(viewBinding: ItemAccountSwitchBinding, position: Int) {
        this.viewBinding = viewBinding
        viewBinding.account.setListener { listener.invoke(detectedAccountable.authId) }
        viewBinding.account.apply {
            setLinkItemText(detectedAccountable.residenceName)
        }
        viewBinding.account.setCheck(isCurrentAccount)
    }

    override fun getLayout(): Int = R.layout.item_account_switch

    override fun initializeViewBinding(view: View): ItemAccountSwitchBinding {
        return ItemAccountSwitchBinding.bind(view)
    }
}