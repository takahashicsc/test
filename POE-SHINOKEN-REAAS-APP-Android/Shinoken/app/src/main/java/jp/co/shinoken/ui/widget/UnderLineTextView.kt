package jp.co.shinoken.ui.widget

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ViewUnderLineTextBinding

class UnderLineTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = ViewUnderLineTextBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.UnderLineTextView,
            0, 0
        ).apply {
            try {
                binding.underLineText.apply {
                    paintFlags = this.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                    text = getString(R.styleable.UnderLineTextView_underLineText)
                }
            } finally {
                recycle()
            }
        }
    }

    fun setUnderLineText(text: String) {
        binding.underLineText.text = text
    }
}