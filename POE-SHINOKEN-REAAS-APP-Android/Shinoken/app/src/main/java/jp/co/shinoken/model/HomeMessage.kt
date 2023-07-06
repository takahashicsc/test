package jp.co.shinoken.model

import jp.co.shinoken.R
import java.time.LocalTime

class HomeMessage(
    private val currentDateTime: LocalTime
) {
    val homeMessageRes: Int
        get() {
            return when {
                currentDateTime.isAfter(
                    LocalTime.of(
                        11,
                        59,
                        59
                    )
                ) && currentDateTime.isBefore(
                    LocalTime.of(
                        15,
                        59,
                        59
                    )
                ) -> {
                    R.string.home_message_daytime
                }

                currentDateTime.isAfter(
                    LocalTime.of(
                        4,
                        59,
                        59
                    )
                ) && currentDateTime.isBefore(LocalTime.of(11, 59, 59)) -> {
                    R.string.home_message_morning
                }
                else -> {
                    R.string.home_message_night
                }
            }
        }
}