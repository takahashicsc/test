package jp.co.shinoken.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ViewLinkItemBinding

class LinkItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = ViewLinkItemBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LinkItemView,
            0, 0
        ).apply {
            try {
                binding.linkItemText.apply {
                    text = getString(R.styleable.LinkItemView_linkItemText)
                }

            } finally {
                recycle()
            }
        }
    }

    fun setLinkItemText(linkItemText: String) {
        binding.linkItemText.text = linkItemText
    }
}