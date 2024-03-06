package com.rasel.androidbaseapp.ui.auth

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class LoginFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Before
    fun setup() {
//        hiltRule.inject()
        launchFragmentInHiltContainer<LoginFragment>()

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
    fun check_error() {
        // Espresso.closeSoftKeyboard()
        onView(withId(R.id.et_password)).perform(typeText("Steve"))
        onView(withId(R.id.et_username)).perform(typeText("Steve"))
        onView(withId(R.id.btnNext)).perform(click())

        /* val text = hiltRule.getActivity().getString(R.string.error_text)
         onView(withText(text)).check(matches(isDisplayed()))*/
        onView(withText("Password must contain at least 8 characters\n")).check(matches(isDisplayed()))
    }
}



