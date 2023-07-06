package jp.co.shinoken.extension

import java.util.*

fun Date.differenceDays(diffDate: Date): Int {
    val datetime1 = this.time
    val datetime2 = diffDate.time
    val diffDays = (datetime1 - datetime2) / (1000 * 60 * 60 * 24)
    return diffDays.toInt()
}