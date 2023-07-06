package jp.co.shinoken.ui.fragment.home.item

import android.view.View
import androidx.core.view.isVisible
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemHomeMenuBinding
import jp.co.shinoken.model.api.Menu

class HomeMenuItem(
    private val homeMenu: Menu,
    private val listener: () -> Unit
) : BindableItem<ItemHomeMenuBinding>() {
    override fun bind(viewBinding: ItemHomeMenuBinding, position: Int) {
        viewBinding.layout.setOnClickListener {
            listener.invoke()
        }
        viewBinding.menuText.text = homeMenu.title
        viewBinding.menuDescription.apply {
            isVisible = homeMenu.subTitle.isNullOrEmpty().not()
            text = homeMenu.subTitle
        }
        viewBinding.menuIcon.setImageResource(homeMenu.slug.iconRes)
        viewBinding.badgeIcon.isVisible = homeMenu.new
    }

    override fun getLayout(): Int = R.layout.item_home_menu

    override fun initializeViewBinding(view: View): ItemHomeMenuBinding {
        return ItemHomeMenuBinding.bind(view)
    }
}