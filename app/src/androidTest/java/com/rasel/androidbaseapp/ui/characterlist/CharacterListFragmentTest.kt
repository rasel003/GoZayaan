package com.rasel.androidbaseapp.ui.characterlist


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CharacterListFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Before
    fun setup() {
//        hiltRule.inject()
        launchFragmentInHiltContainer<CharacterListFragment>()

//        scenario = launchFragmentInContainer(themeResId = R.style.Theme_App_DayNight)
//        scenario.moveToState(Lifecycle.State.STARTED)
    }


    @Test
    fun switch_to_english_should_return_english_localization() {
        // Espresso.closeSoftKeyboard()
        onView(withId(R.id.btnEnglish)).perform(click())

        //Assertion
        onView(withId(R.id.btnEnglish)).check(matches(withText("English")))
    }

    @Test
    fun switch_to_chinese_should_return_chinese_localization() = runTest {
        // Espresso.closeSoftKeyboard()
        onView(withId(R.id.btnChinese)).perform(click())

        //Assertion
        onView(withId(R.id.btnEnglish)).check(matches(withText("英语")))
    }

    @Test
    fun switch_to_burmese_should_return_burmese_localization() {
        // Espresso.closeSoftKeyboard()
        onView(withId(R.id.btnBurmese)).perform(click())

        //Assertion
        onView(withId(R.id.btnEnglish)).check(matches(withText("အင်္ဂလိပ်")))
    }
}