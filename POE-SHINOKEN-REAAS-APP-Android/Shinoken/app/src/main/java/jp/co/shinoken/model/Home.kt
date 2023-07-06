package jp.co.shinoken.model

class Home(
    val suggestions: List<Suggestion>,
    val homeMenus: List<HomeMenu>,
    val user: User,
    val weather: Weather,
) {
    data class Suggestion(
        val title: String,
        val iconRes: Int
    )

    data class Weather(
        val name: String,
        val iconRes: Int
    )
}