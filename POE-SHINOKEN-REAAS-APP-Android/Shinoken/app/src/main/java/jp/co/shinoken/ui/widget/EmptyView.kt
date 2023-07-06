package jp.co.shinoken.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ViewEmptyBinding

class EmptyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = ViewEmptyBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EmptyView,
            0, 0
        ).apply {
            try {
                binding.emptyText.apply {
                    text = getString(R.styleable.EmptyView_emptyText)
                }
            } finally {
                recycle()
            }
        }
    }

    fun setEmptyText(text: String) {
        binding.emptyText.text = text
    }
}