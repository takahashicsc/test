package jp.co.shinoken.ui.fragment.home.item

import android.view.View
import androidx.core.net.toUri
import coil.load
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemHomeMenuHeaderChildBinding
import jp.co.shinoken.model.api.Suggestion


class HomeSuggestionItem(
    private val suggestion: Suggestion,
    private val listener: (Suggestion) -> Unit
) : BindableItem<ItemHomeMenuHeaderChildBinding>() {
    override fun bind(viewBinding: ItemHomeMenuHeaderChildBinding, position: Int) {
        viewBinding.itemLayout.setOnClickListener {
            listener.invoke(suggestion)
        }
        suggestion.image?.let {
            viewBinding.itemImg.load(it.publishUrl.toUri())
        }
        viewBinding.itemText.text = suggestion.title
    }

    override fun getLayout(): Int = R.layout.item_home_menu_header_child

    override fun initializeViewBinding(view: View): ItemHomeMenuHeaderChildBinding {
        return ItemHomeMenuHeaderChildBinding.bind(view)
    }
}