package jp.co.shinoken.ui.fragment.lifeline_contact.item

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemLifelineBinding
import jp.co.shinoken.model.api.Me

class LifelineItem(
    private val lifeline: Me.Lifeline,
    private val listener: (String) -> Unit
) : BindableItem<ItemLifelineBinding>() {
    override fun bind(viewBinding: ItemLifelineBinding, position: Int) {
        viewBinding.lifelineLayout.setOnClickListener {
            listener.invoke(lifeline.tel)
        }
        viewBinding.lifelineKind.text = lifeline.kind
        viewBinding.lifelineName.text = lifeline.name
        viewBinding.lifelinePhoneNumber.text = lifeline.tel
    }

    override fun getLayout(): Int = R.layout.item_lifeline

    override fun initializeViewBinding(view: View): ItemLifelineBinding {
        return ItemLifelineBinding.bind(view)
    }
}