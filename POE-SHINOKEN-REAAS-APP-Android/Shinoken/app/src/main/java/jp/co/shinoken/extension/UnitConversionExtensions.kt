package jp.co.shinoken.extension

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

fun Int.convertPxToDp(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this / scale + 0.5f).toInt()
}

fun Int.convertDpToPx(context: Context): Float {
    val scale = context.resources.displayMetrics.density
    return (this * scale + 0.5f)
}

fun String.convertStringToDate(): Date? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.JAPAN)
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    return dateFormat.parse(this)
}

fun Date.convertDateToString(format: String): String {
    val dateFormat = SimpleDateFormat(format, Locale.JAPAN)
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    return dateFormat.format(this)
}