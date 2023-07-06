package jp.co.shinoken.ui.fragment.faq.item

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemFaqContentBinding
import jp.co.shinoken.model.FaqContent

class FaqContentItem(
    private val faqContent: FaqContent,
    private val listener: (faqId: Int) -> Unit
) : BindableItem<ItemFaqContentBinding>() {
    override fun bind(viewBinding: ItemFaqContentBinding, position: Int) {
        viewBinding.faqTitle.text = faqContent.title
        viewBinding.faqLayout.setOnClickListener {
            listener.invoke(faqContent.id)
        }
    }

    override fun getLayout(): Int = R.layout.item_faq_content

    override fun initializeViewBinding(view: View): ItemFaqContentBinding {
        return ItemFaqContentBinding.bind(view)
    }
}