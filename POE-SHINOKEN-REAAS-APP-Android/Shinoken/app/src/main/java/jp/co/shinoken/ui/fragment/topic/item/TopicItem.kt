package jp.co.shinoken.ui.fragment.topic.item

import android.view.View
import coil.load
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemTopicBinding
import jp.co.shinoken.model.Topic

class TopicItem(
    private val topic: Topic,
    private val listener: (String) -> Unit
) : BindableItem<ItemTopicBinding>() {
    override fun bind(viewBinding: ItemTopicBinding, position: Int) {
        viewBinding.topicLayout.setOnClickListener {
            listener.invoke(topic.url)
        }
        topic.image?.let {
            viewBinding.topicImg.load(it)
        }
        viewBinding.topicTitle.text = topic.title
        viewBinding.topicDate.text = topic.date
    }

    override fun getLayout(): Int = R.layout.item_topic

    override fun initializeViewBinding(view: View): ItemTopicBinding {
        return ItemTopicBinding.bind(view)
    }
}