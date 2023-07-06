package jp.co.shinoken.extension

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import jp.co.shinoken.BuildConfig
import jp.co.shinoken.R
import jp.co.shinoken.activity.MainActivity
import jp.co.shinoken.model.AppError
import jp.co.shinoken.ui.fragment.home.HomeFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun Fragment.showAlertDialog(
    title: String? = null,
    message: String,
    positiveText: String = "OK",
    positiveListener: (() -> Unit)? = null,
    negativeText: String? = null,
    negativeListener: (() -> Unit)? = null
) {
    AlertDialog.Builder(requireContext()).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveText) { _, _ -> positiveListener?.invoke() }
        negativeText?.let {
            setNegativeButton(negativeText) { _, _ -> negativeListener?.invoke() }
        }
    }.show()
}

fun Fragment.showApiAlertError(
    title: String = getString(R.string.network_error_default_title),
    appError: AppError,
    positiveListener: (() -> Unit)? = null,
) {
    showAlertDialog(
        title = when (appError){
            is AppError.CognitoException -> {
                "${appError.cognitoErrorMessages.title}"
            }
            is AppError.ApiException.AlertAppError -> null
            else -> {
                "${title}"
            }
                               },
        message = when (appError) {
            is AppError.ApiException.AlertAppError -> {
                "${appError.alertError.message}\n${"(%s)".format(appError.alertError.errorMessageSuffixCode)}"
            }
            is AppError.ApiException.JsonDataException -> {
                "${getString(R.string.network_error_default_message)}\n(E8888)${if (BuildConfig.FLAVOR == "develop" || BuildConfig.FLAVOR == "staging") "\n\n以下本番では表示されません。\n${appError.message}" else ""}"
            }
            is AppError.CognitoException -> {
                appError.cognitoErrorMessages.message
            }

            else -> {
                "${getString(R.string.network_error_default_message)}\nE009"
            }
        },
        positiveListener = positiveListener
    )
}

fun Fragment.showSnackBar(
    view: View,
    message: String
) {
    val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT).apply {
        this.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
    }
    snackBar.show()
}

fun Fragment.showChromeCustomTabs(uri: Uri) {
    CustomTabsIntent.Builder().setShowTitle(true).setToolbarColor(
        ContextCompat.getColor(
            requireContext(),
            R.color.white
        )
    ).build().run {
        launchUrl(requireActivity(), uri)
    }
}

fun Fragment.showBrowser(uri: Uri) {
    startActivity(Intent(Intent.ACTION_VIEW, uri))
}


fun Fragment.actionTelPhone(telNumber: String) {
    val telSchemeNumber = "tel:${telNumber}".toUri()
    startActivity(Intent(Intent.ACTION_DIAL, telSchemeNumber))
}

fun Fragment.hideKeyboard() {
    val inputMethodManager =
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(
        requireActivity().currentFocus?.windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}

fun Fragment.appError(appError: AppError, reloadListener: (() -> Unit)? = null) {
    when (appError) {
        is AppError.UserIdNotExistsError -> {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
        }
        is AppError.ArgumentNotException -> {
            requireActivity().onBackPressed()
        }
        is AppError.ApiException.AuthError -> {
            signOut()
        }

        is AppError.ApiException.UnKnownHostError -> {
            val positiveText = if (reloadListener != null) getString(R.string.reload) else "OK"
            showAlertDialog(
                title = getString(R.string.network_error_default_title),
                message = "${getString(R.string.network_error_default_message)}\n${if (reloadListener != null) "(E0010)" else "(E0020)"}",
                positiveListener = { reloadListener?.invoke() },
                positiveText = positiveText,
                negativeText = if (reloadListener != null) getString(R.string.cancel) else null
            )
        }

        else -> {
            showApiAlertError(appError = appError)
        }
    }
}

fun Fragment.signOut() {
    val activity = requireActivity()
    if (activity !is MainActivity) return
    activity.signOut()
}

suspend fun Fragment.convertUriToFile(uri: Uri): File? {
    val file = getFile(uri) ?: return null
    return file
}

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun Fragment.getFile(uri: Uri): File? = withContext(Dispatchers.IO) {
    val bitmap = if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(
            requireActivity().contentResolver,
            uri
        )
    } else {
        val source = ImageDecoder.createSource(requireActivity().contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
    val documentFile = DocumentFile.fromSingleUri(requireContext(), uri)
    val fileName = documentFile?.name ?: return@withContext null
    val insertFile = File(context!!.cacheDir, fileName)
    insertFile.createNewFile()
    val bos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
    val bitmapData = bos.toByteArray()
    val fos = FileOutputStream(insertFile)
    try {
        fos.write(bitmapData)
        fos.flush()
        fos.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return@withContext insertFile
}