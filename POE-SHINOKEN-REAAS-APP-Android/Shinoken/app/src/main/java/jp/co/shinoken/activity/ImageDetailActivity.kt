package jp.co.shinoken.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.igreenwood.loupe.Loupe
import com.igreenwood.loupe.extensions.createLoupe
import com.igreenwood.loupe.extensions.setOnViewTranslateListener
import jp.co.shinoken.R
import kotlinx.coroutines.launch

class ImageDetailActivity : AppCompatActivity() {
    private val imageUrl: String by lazy {
        intent?.getStringExtra(ArgImageUrl)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        lifecycleScope.launch {
            val imageView =
                findViewById<ImageView>(R.id.image_view).apply { load(imageUrl).await() }

            val imageContent = findViewById<FrameLayout>(R.id.image_content)
            createLoupe(imageView, imageContent) {
                useFlingToDismissGesture = !LoupePrefs.useSharedElements
                maxZoom = LoupePrefs.maxZoom
                flingAnimationDuration = LoupePrefs.flingAnimationDuration
                scaleAnimationDuration = LoupePrefs.scaleAnimationDuration
                overScaleAnimationDuration = LoupePrefs.overScaleAnimationDuration
                overScrollAnimationDuration = LoupePrefs.overScrollAnimationDuration
                viewDragFriction = LoupePrefs.viewDragFriction
                dismissAnimationDuration = LoupePrefs.dismissAnimationDuration
                restoreAnimationDuration = LoupePrefs.restoreAnimationDuration

                setOnViewTranslateListener(
                    onStart = { hideToolbar() },
                    onRestore = { showToolbar() },
                    onDismiss = { finishAfterTransition() }
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showToolbar() {
        supportActionBar?.show()
    }

    private fun hideToolbar() {
        supportActionBar?.hide()
    }

    companion object {
        private const val ArgImageUrl = "ArgImageUrl"

        object LoupePrefs {
            var useSharedElements = true
            var maxZoom = Loupe.DEFAULT_MAX_ZOOM
            var flingAnimationDuration = Loupe.DEFAULT_ANIM_DURATION
            var scaleAnimationDuration = Loupe.DEFAULT_ANIM_DURATION_LONG
            var overScaleAnimationDuration = Loupe.DEFAULT_ANIM_DURATION_LONG
            var overScrollAnimationDuration = Loupe.DEFAULT_ANIM_DURATION
            var dismissAnimationDuration = Loupe.DEFAULT_ANIM_DURATION
            var restoreAnimationDuration = Loupe.DEFAULT_ANIM_DURATION
            var viewDragFriction = Loupe.DEFAULT_VIEW_DRAG_FRICTION
        }

        fun createIntent(
            context: Context,
            imageUrl: String
        ): Intent =
            Intent(context, ImageDetailActivity::class.java).apply {
                putExtra(ArgImageUrl, imageUrl)
            }
    }
}

