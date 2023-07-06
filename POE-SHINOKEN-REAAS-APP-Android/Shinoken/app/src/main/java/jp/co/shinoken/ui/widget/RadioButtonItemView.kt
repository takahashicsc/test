package jp.co.shinoken.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ViewRadioItemBinding


class RadioButtonItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = ViewRadioItemBinding.inflate(LayoutInflater.from(context), this, true)
    private var isChecked: Boolean = false

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.RadioItemView,
            0, 0
        ).apply {
            try {
                binding.radioItemText.apply {
                    text = getString(R.styleable.RadioItemView_radioItemText)
                }
                binding.radioItem.isChecked = isChecked
                binding.radioItemLayout.setOnClickListener {
                    binding.radioItem.isChecked = binding.radioItem.isChecked.not()
                }
            } finally {
                recycle()
            }
        }
    }

    fun setListener(listener: OnClickListener) {
        binding.radioItemLayout.setOnClickListener(listener)
    }

    fun setCheck(isCheck: Boolean) {
        binding.radioItem.isChecked = isCheck
    }

    fun setLinkItemText(linkItemText: String) {
        binding.radioItemText.text = linkItemText
    }
}