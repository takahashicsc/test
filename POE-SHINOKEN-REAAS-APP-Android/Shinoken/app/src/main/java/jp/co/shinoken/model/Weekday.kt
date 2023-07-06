package jp.co.shinoken.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import jp.co.shinoken.R

enum class Weekday(@StringRes val resText: Int, @DrawableRes val resBackGround: Int) {
    Sun(R.string.sunday, R.drawable.bg_week_sunday),
    Mon(R.string.monday, R.drawable.bg_week_monday),
    Tue(R.string.tuesday, R.drawable.bg_week_tuesday),
    Wed(R.string.wednesday, R.drawable.bg_week_wednesday),
    Thu(R.string.thursday, R.drawable.bg_week_thursday),
    Fri(R.string.friday, R.drawable.bg_week_friday),
    Sat(R.string.saturday, R.drawable.bg_week_saturday);
}