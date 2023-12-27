package com.rasel.androidbaseapp.ui.plant_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.MediumTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.rasel.androidbaseapp.EspressoIdlingResourceRule
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.mockito.Mockito
import java.lang.Thread.sleep
import java.util.concurrent.CompletableFuture.allOf
import java.util.regex.Pattern.matches


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4ClassRunner::class)
class PlantListFragmentTest {

    val LIST_ITEM_IN_TEST = 0


    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Before
    fun setup() {
        hiltRule.inject()
        // Register the IdlingResource if you're using it
        IdlingRegistry.getInstance().register(RecyclerViewIdlingResource.getIdlingResource())

    }
    @Test
    fun a_test_isListFragmentVisible_onAppLaunch() {
        onView(withId(R.id.plant_list)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

//        onView(withId(R.id.progress_bar)).check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
    }
    @Test
    fun test_backNavigation_toMovieListFragment() {

        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<PlantListFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        // Click list item #LIST_ITEM_IN_TEST
        onView(withId(R.id.plant_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<PlantAdapter.PlantViewHolder>(
                    LIST_ITEM_IN_TEST,
                    click()
                )
            )

        // Confirm nav to DetailFragment and display title
        onView(withId(R.id.plant_detail_name)).check(ViewAssertions.matches(withText("Apple")))

        Espresso.pressBack()

        // Confirm MovieListFragment in view
        onView(withId(R.id.plant_list)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
    @Test
    fun clickAddShoppingItemButton_navigateToAddShoppingItemFragment() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<PlantListFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        // Increment IdlingResource before fetching data
        RecyclerViewIdlingResource.increment()

        // Perform actions to fetch data (e.g., perform a click or wait for the data to load)
        sleep(10000)

        // Decrement IdlingResource when data is loaded
        RecyclerViewIdlingResource.decrement()

//        Espresso.onView(withId(R.id.tvTest)).perform(ViewActions.click())
        /*Espresso.onView(withId(R.id.plant_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PlantAdapter.PlantViewHolder>(
                0,
                ViewActions.click()
            )
        )*/

       /* val position = 4
        Espresso.onView(withId(R.id.plant_list))
            .perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    position,
                    ViewActions.click()
                )
            )*/

//        waitUntilLoaded { fragment.recycler }

        // Perform assertions on the RecyclerView
       /* onView(withId(R.id.plant_list))
            .check(matches(allOf(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                isAssignableFrom(RecyclerView::class.java),
                hasDescendant(withId(R.id.plant_item_image)) // Replace with the ID of a view within your RecyclerView item layout
            )))*/



        Espresso.onView(withId(R.id.plant_list)).perform(ViewActions.scrollTo(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,click()));
        Mockito.verify(navController).navigate(
            PlantListFragmentDirections.actionPlantListFragmentToPlantDetailFragment(
                "bougainvillea-glabra"
            )
        )
    }

    /**
     * Stop the test until RecyclerView's data gets loaded.
     *
     * Passed [recyclerProvider] will be activated in UI thread, allowing you to retrieve the View.
     *
     * Workaround for https://issuetracker.google.com/issues/123653014
     */
    private inline fun waitUntilLoaded(crossinline recyclerProvider: () -> RecyclerView) {
        Espresso.onIdle()

        lateinit var recycler: RecyclerView

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            recycler = recyclerProvider()
        }

        while (recycler.hasPendingAdapterUpdates()) {
            Thread.sleep(10)
        }
    }

    @After
    fun tearDown() {
        // Unregister the IdlingResource if you're using it
        IdlingRegistry.getInstance().unregister(RecyclerViewIdlingResource.getIdlingResource())
    }
}

object RecyclerViewIdlingResource {

    private const val RESOURCE = "RECYCLER_VIEW_IDLING_RESOURCE"

    private val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        countingIdlingResource.decrement()
    }

    fun getIdlingResource(): IdlingResource {
        return countingIdlingResource
    }
}