package jp.co.shinoken.ui.fragment.search.item

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemSearchResultBinding
import jp.co.shinoken.model.Tag

class SearchResultItem(
    private val tag: Tag,
    private val listener: (Int) -> Unit
) : BindableItem<ItemSearchResultBinding>() {
    override fun bind(viewBinding: ItemSearchResultBinding, position: Int) {
        viewBinding.searchResultLayout.setOnClickListener {
            listener.invoke(tag.pageId)
        }
        viewBinding.tagName.text = tag.name
    }

    override fun getLayout(): Int = R.layout.item_search_result

    override fun initializeViewBinding(view: View): ItemSearchResultBinding {
        return ItemSearchResultBinding.bind(view)
    }
}