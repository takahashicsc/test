package jp.co.shinoken.ui.fragment.notification.boards.item

import android.view.View
import androidx.core.view.isVisible
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemNotificationBinding
import jp.co.shinoken.model.api.Board

class BoardItem(
    private val board: Board,
    private val listener: (Int) -> Unit
) : BindableItem<ItemNotificationBinding>() {
    override fun bind(viewBinding: ItemNotificationBinding, position: Int) {
        viewBinding.notificationItemLayout.setOnClickListener {
            listener.invoke(board.id)
        }
        viewBinding.notificationDate.apply {
            isVisible = board.startAt != null
            text = board.getStartAtFormatString()
        }
        viewBinding.notificationTitleBadge.visibility = View.GONE
        viewBinding.notificationTitle.text = board.title
    }

    override fun getLayout(): Int = R.layout.item_notification

    override fun initializeViewBinding(view: View): ItemNotificationBinding {
        return ItemNotificationBinding.bind(view)
    }
}