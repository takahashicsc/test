package jp.co.shinoken.model

import java.io.Serializable

data class BirthDay(val year: Int?, val month: Int?, val day: Int?) : Serializable {
    private val birthDayFormat = "%02d"

    val birthDayHyphenString: String?
        get() {
            if (year == null || month == null || day == null) return null
            return "${year}-${birthDayFormat.format(month)}-${birthDayFormat.format(day)}"
        }

    fun getBirthDayFormatString(formatString: String): String? {
        if (year == null || month == null || day == null) return null
        return formatString.format(year, month, day)
    }

    companion object {
        fun parseBirthDay(birthDayString: String?): BirthDay? {
            if (birthDayString.isNullOrEmpty()) return null
            val parseString = birthDayString.split("-")
            return BirthDay(parseString[0].toInt(), parseString[1].toInt(), parseString[2].toInt())
        }
    }
}