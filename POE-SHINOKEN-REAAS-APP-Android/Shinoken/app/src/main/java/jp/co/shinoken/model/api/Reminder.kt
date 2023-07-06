package jp.co.shinoken.model.api

import com.squareup.moshi.JsonClass
import jp.co.shinoken.model.Weekday


@JsonClass(generateAdapter = true)
data class Reminder(
    val before: Before,
    val hour: Int,
    val minute: Int,
    val name: String,
    val mode: Mode
) {
    sealed class Mode {
        class Weekly(val weekdays: List<Weekday>) : Mode()
        class Custom(val week: Int, val weekdays: List<Weekday>) : Mode()
        class Monthly(val day: Int) : Mode()
    }

    enum class Before {
        Day0,
        Day1
    }
}
