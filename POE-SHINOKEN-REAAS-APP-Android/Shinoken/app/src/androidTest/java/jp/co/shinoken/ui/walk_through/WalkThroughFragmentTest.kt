package jp.co.shinoken.ui.walk_through

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.co.shinoken.R
import jp.co.shinoken.ui.fragment.walk_through.WalkThroughFragment
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SampleFragmentTest {
    @Test
    fun `launchFragment_初回表示は戻るボタン非表示`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        launchFragmentInContainer<WalkThroughFragment>(null, R.style.Theme_Shinoken)
        onView(withId(R.id.next_button)).check(matches(withText(context.getString(R.string.next))))
        onView(withId(R.id.back_button)).check(matches(not(isDisplayed())))
    }

    @Test
    fun `launchFragment_一度目のクリックで戻るボタン表示`() {
        launchFragmentInContainer<WalkThroughFragment>(null, R.style.Theme_Shinoken)
        onView(withId(R.id.next_button)).perform(click())
        onView(withId(R.id.back_button)).check(matches((isDisplayed())))
    }

    @Test
    fun `launchFragment_3ページ目で次へボタンのテキスト変更`() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        launchFragmentInContainer<WalkThroughFragment>(null, R.style.Theme_Shinoken)
        onView(withId(R.id.next_button)).perform(click())
        onView(withId(R.id.next_button)).perform(click())
        onView(withId(R.id.next_button)).perform(click())
        onView(withId(R.id.next_button)).check(matches(withText(context.getString(R.string.start))))
    }
}