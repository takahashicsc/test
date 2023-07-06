package jp.co.shinoken.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ViewDescriptionLinkItemBinding

class LinkDescriptionItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding =
        ViewDescriptionLinkItemBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LinkDescriptionItemView,
            0, 0
        ).apply {
            try {
                binding.linkItemText.apply {
                    text = getString(R.styleable.LinkDescriptionItemView_linkText)
                }

                val description = getString(R.styleable.LinkDescriptionItemView_linkDescriptionText)
                binding.linkItemDescription.isVisible = description.isNullOrEmpty().not()
                description?.let {
                    binding.linkItemDescription.text = it
                }

                val linkIcon = getDrawable(R.styleable.LinkDescriptionItemView_linkIcon)
                linkIcon?.let {
                    binding.linkItemImg.setImageDrawable(it)
                }
            } finally {
                recycle()
            }
        }
    }

    fun setLinkItemIcon(@DrawableRes icon: Int) {
        binding.linkItemImg.setImageResource(icon)
    }

    fun setLinkItemText(linkItemText: String) {
        binding.linkItemText.text = linkItemText
    }

    fun setLinkItemDescription(linkItemDescription: String) {
        binding.linkItemDescription.text = linkItemDescription
    }
}