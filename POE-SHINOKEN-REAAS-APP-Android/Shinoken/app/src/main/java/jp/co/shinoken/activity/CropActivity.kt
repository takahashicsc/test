package jp.co.shinoken.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.theartofdev.edmodo.cropper.CropImageView
import com.theartofdev.edmodo.cropper.CropImageView.OnCropImageCompleteListener
import com.theartofdev.edmodo.cropper.CropImageView.OnSetImageUriCompleteListener
import jp.co.shinoken.BuildConfig
import jp.co.shinoken.databinding.ActivityCropBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class CropActivity : AppCompatActivity(), OnSetImageUriCompleteListener,
    OnCropImageCompleteListener {
    private val uri: Uri? by lazy {
        intent?.getParcelableExtra(ArgsAreaList)
    }

    private lateinit var binding: ActivityCropBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCropBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.cropImageView.apply {
            setImageUriAsync(uri)
            setOnSetImageUriCompleteListener(this@CropActivity)
            setOnCropImageCompleteListener(this@CropActivity)
        }

        binding.cropButton.setOnClickListener {
            binding.cropImageView.getCroppedImageAsync()
        }
        binding.cancelButton.setOnClickListener {
            onBackPressed()
        }
    }

    companion object {
        private const val ArgsAreaList = "ArgsAreaList"
        fun createIntent(
            context: Context,
            uri: Uri
        ): Intent = Intent(context, CropActivity::class.java).apply {
            putExtra(ArgsAreaList, uri)
        }
    }

    override fun onSetImageUriComplete(view: CropImageView?, uri: Uri?, error: Exception?) {
        Timber.d(uri.toString())
    }

    override fun onCropImageComplete(view: CropImageView?, result: CropImageView.CropResult?) {
        result?.let {
            if (result.uri != null) {
                val intent = Intent().apply {
                    putExtra(MainActivity.ResultUri, uri)
                }
                setResult(RESULT_OK, intent)
            } else {
                lifecycleScope.launch(Dispatchers.IO) {
                    binding.cropImageView.isShowProgressBar = true
                    val timeStamp = SimpleDateFormat(
                        "yyyy_MM_dd_hh_mm_ss",
                        Locale.JAPAN
                    ).format(Date())
                    val imageName = "$timeStamp.jpeg"
                    val imageFile = File(cacheDir, imageName)
                    val out: FileOutputStream
                    try {
                        out = FileOutputStream(imageFile)
                        result.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        out.close()
                        ///画像をアプリの内部領域に保存
                        val contentUri = FileProvider.getUriForFile(
                            this@CropActivity,
                            BuildConfig.APPLICATION_ID + ".provider",
                            imageFile
                        )
                        val intent = Intent().apply {
                            putExtra(MainActivity.ResultUri, contentUri)
                        }
                        setResult(RESULT_OK, intent)
                        finish()
                    } catch (e: FileNotFoundException) {
                        Timber.e(e)
                    }
                }
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

    override fun onBackPressed() {
        finish()
    }
}