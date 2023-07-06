package jp.co.shinoken.ui.fragment.faq.item

import android.view.View
import coil.load
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemFaqCategoryBinding
import jp.co.shinoken.model.FaqCategory


class FaqCategoryItem(
    private val faqCategoryItem: FaqCategory,
    private val listener: (FaqCategory) -> Unit
) : BindableItem<ItemFaqCategoryBinding>(faqCategoryItem.id.toLong()) {
    override fun bind(viewBinding: ItemFaqCategoryBinding, position: Int) {
        faqCategoryItem.image?.let {
            viewBinding.categoryIcon.load(it.publishUrl)
        }

        viewBinding.categoryName.text = faqCategoryItem.name

        viewBinding.categoryLayout.setOnClickListener {
            listener.invoke(faqCategoryItem)
        }

        viewBinding.categoryIcon.isSelected = faqCategoryItem.isSelected
        viewBinding.categoryLayout.isEnabled = faqCategoryItem.isSelected.not()
    }

    override fun getLayout(): Int = R.layout.item_faq_category

    override fun initializeViewBinding(view: View): ItemFaqCategoryBinding {
        return ItemFaqCategoryBinding.bind(view)
    }
}