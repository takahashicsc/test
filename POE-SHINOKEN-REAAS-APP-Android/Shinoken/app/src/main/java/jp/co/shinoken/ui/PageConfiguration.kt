package jp.co.shinoken.ui

import androidx.annotation.IdRes
import jp.co.shinoken.R

enum class PageConfiguration(
    val id: Int,
    val hideToolbar: Boolean = false
) {
    Home(R.id.homeFragment, true),
    Search(R.id.searchFragment, true),
    WalkThrough(R.id.walkThroughFragment, true),
    Login(R.id.loginFragment, true),
    Other(0);

    companion object {
        fun getConfiguration(@IdRes id: Int): PageConfiguration {
            return PageConfiguration
                .values()
                .firstOrNull { it.id == id } ?: Other
        }
    }
}