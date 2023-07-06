package jp.co.shinoken.ui.fragment.notification.notifications.item

import android.view.View
import androidx.core.view.isVisible
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemNotificationBinding
import jp.co.shinoken.model.api.Notification

class NotificationItem(
    private val notification: Notification,
    private val listener: (Int) -> Unit
) : BindableItem<ItemNotificationBinding>((notification.id.toLong())) {
    override fun bind(viewBinding: ItemNotificationBinding, position: Int) {
        viewBinding.notificationItemLayout.setOnClickListener {
            listener.invoke(notification.id)
        }
        viewBinding.notificationDate.apply {
            isVisible = notification.segmentedAt != null
            text = notification.getSegmentedAtFormatString()
        }
        viewBinding.notificationTitle.text = notification.title
        viewBinding.notificationTitleBadge.isVisible = notification.read.not()
    }

    override fun getLayout(): Int = R.layout.item_notification

    override fun initializeViewBinding(view: View): ItemNotificationBinding {
        return ItemNotificationBinding.bind(view)
    }
}