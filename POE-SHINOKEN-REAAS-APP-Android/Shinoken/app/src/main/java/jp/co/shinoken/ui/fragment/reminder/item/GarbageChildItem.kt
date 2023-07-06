package jp.co.shinoken.ui.fragment.reminder.item

import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemReminderChildBinding
import jp.co.shinoken.model.api.Reminder

class GarbageChildItem(
    private val reminder: Reminder,
    private val listener: () -> Unit
) : BindableItem<ItemReminderChildBinding>() {
    override fun bind(viewBinding: ItemReminderChildBinding, position: Int) {
        val mode = reminder.mode
        viewBinding.garbageTag.apply {
            val backgroundRes =
                when (mode) {
                    is Reminder.Mode.Weekly -> {
                        mode.weekdays[0].resBackGround
                    }
                    is Reminder.Mode.Custom -> {
                        mode.weekdays[0].resBackGround
                    }
                    else -> {
                        R.drawable.bg_week_sunday
                    }
                }
            background = ResourcesCompat.getDrawable(
                context.resources,
                backgroundRes,
                null
            )

            text = when (mode) {
                is Reminder.Mode.Weekly -> context.getString(R.string.format_weekly)
                    .format(context.getString(mode.weekdays[0].resText))
                is Reminder.Mode.Custom -> context.getString(R.string.format_specific_interval)
                    .format(
                        mode.week,
                        context.getString(mode.weekdays[0].resText)
                    )
                is Reminder.Mode.Monthly -> context.getString(R.string.format_monthly)
                    .format(mode.day)
            }
        }
        viewBinding.garbageTitle.text = reminder.name

        viewBinding.garbageTime.apply {
            val beforeText =
                context.getString(if (reminder.before == Reminder.Before.Day0) R.string.before_0day else R.string.before_1day)
            text = context.getString(R.string.format_time)
                .format(beforeText, reminder.hour, reminder.minute)
        }

        viewBinding.garbageChildLayout.setOnClickListener {
            listener.invoke()
        }
    }

    override fun getLayout(): Int = R.layout.item_reminder_child

    override fun initializeViewBinding(view: View): ItemReminderChildBinding {
        return ItemReminderChildBinding.bind(view)
    }
}