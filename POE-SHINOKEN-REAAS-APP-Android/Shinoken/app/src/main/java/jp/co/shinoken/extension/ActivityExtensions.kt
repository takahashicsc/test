package jp.co.shinoken.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import jp.co.shinoken.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

fun AppCompatActivity.showAlertDialog(
    title: String? = null,
    message: String,
    positiveText: String = "OK",
    positiveListener: (() -> Unit)? = null,
    negativeText: String? = null,
    negativeListener: (() -> Unit)? = null,
    cancelable: Boolean = true
) {
    AlertDialog.Builder(this).apply {
        setCancelable(cancelable)
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveText) { _, _ -> positiveListener?.invoke() }
        negativeText?.let {
            setNegativeButton(negativeText) { _, _ -> negativeListener?.invoke() }
        }
    }.show()
}

fun AppCompatActivity.getCurrentFragment(fragmentId: Int): Fragment? {
    val navHostFragment: Fragment = supportFragmentManager.findFragmentById(fragmentId)
        ?: return null

    return navHostFragment.childFragmentManager.fragments[0]
}

suspend fun AppCompatActivity.saveCashImage(bitmap: Bitmap): Uri? {
    val timeStamp = SimpleDateFormat(
        "yyyy_MM_dd_hh_mm_ss",
        Locale.JAPAN
    ).format(Date())
    val imageName = "$timeStamp.jpeg"
    val imageFile = File(cacheDir, imageName)
    val out: FileOutputStream

    return withContext(Dispatchers.IO) {
        try {
            out = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
            ///画像をアプリの内部領域に保存
            return@withContext FileProvider.getUriForFile(
                this@saveCashImage,
                BuildConfig.APPLICATION_ID + ".provider",
                imageFile
            )
        } catch (e: FileNotFoundException) {
            Timber.e(e)
            return@withContext null
        }
    }
}

fun AppCompatActivity.uriToBitmap(uri: Uri): Bitmap {
    return if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(
            this.contentResolver,
            uri
        )
    } else {
        val source = ImageDecoder.createSource(this.contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}

fun AppCompatActivity.hideKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(
        currentFocus?.windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}