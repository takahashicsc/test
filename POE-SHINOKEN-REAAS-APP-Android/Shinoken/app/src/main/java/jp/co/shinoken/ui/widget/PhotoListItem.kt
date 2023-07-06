package jp.co.shinoken.ui.widget

import android.view.View
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import coil.load
import com.xwray.groupie.viewbinding.BindableItem
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ItemPhotoListBinding
import jp.co.shinoken.model.api.Image

class PhotoListItem(
    private val isSubmit: Boolean,
    private val image: Image?,
    private val listener: () -> Unit,
    private val removeListener: (Image) -> Unit
) : BindableItem<ItemPhotoListBinding>() {
    override fun bind(viewBinding: ItemPhotoListBinding, position: Int) {

        viewBinding.photoItemPlus.apply {
            setOnClickListener {
                listener.invoke()
            }
            isEnabled = image == null
        }
        image?.let {
            viewBinding.photoItem.load(it.publishUrl.toUri())

            viewBinding.photoRemove.setOnClickListener {
                removeListener.invoke(image)
            }
        }


        viewBinding.photoRemove.apply {
            isVisible = image != null && isSubmit.not()
            isEnabled = image != null && isSubmit.not()
        }
        viewBinding.photoItem.isVisible = image != null
        viewBinding.photoItemPlus.isGone = image != null
    }

    override fun getLayout(): Int = R.layout.item_photo_list

    override fun initializeViewBinding(view: View): ItemPhotoListBinding {
        return ItemPhotoListBinding.bind(view)
    }
}