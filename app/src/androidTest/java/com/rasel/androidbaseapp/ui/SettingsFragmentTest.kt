package com.rasel.androidbaseapp.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Before
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.launchFragmentInHiltContainer
import com.rasel.androidbaseapp.ui.settings.SettingsFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class SettingsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Before
    fun setup() {
        hiltRule.inject()
//        scenario = launchFragmentInContainer(themeResId = R.style.Theme_App_DayNight)
//        scenario.moveToState(Lifecycle.State.STARTED)
    }
    /* @Test
     fun testLaunchFragmentInHiltContainer() {
         val activityScenario: ActivityScenario<MainActivity> = launchActivity()
         activityScenario.onActivity {
             val yourFragment: Fragment = it.supportFragmentManager.fragmentFactory.instantiate(
                 Preconditions.checkNotNull(SettingsFragment::class.java.classLoader),
                 SettingsFragment::class.java.name
             )

             it.supportFragmentManager.beginTransaction()
                 .add(android.R.id.content, yourFragment, "")
                 .commitNow()

         }
     }*/


    @Test
    fun testEventFragment() {
//        val scenario = launchFragmentInContainer<SettingsFragment>()
        launchFragmentInHiltContainer<SettingsFragment> {
//            onView(withId(R.id.btnEnglish)).perform(click())

            //Assertion
//            onView(withId(R.id.btnEnglish)).check(matches(withText("English")))
        }

    }



    @Test
    fun testAddingSpend() {
        /* val amount = 100
         val desc = "Bought Eggs"
         //Espresso Matcher and Action
         onView(withId(R.id.edit_text_amount)).perform(typeText(amount.toString()))
         onView(withId(R.id.edit_text_description)).perform(typeText(desc))*/
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.btnEnglish)).perform(click())

        //Assertion
        onView(withId(R.id.btnEnglish)).check(matches(withText("English")))
    }
}