package com.rasel.androidbaseapp.ui.gallery

import android.app.Application
import android.content.Intent
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.rasel.androidbaseapp.EspressoIdlingResourceRule
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.launchFragmentInHiltContainer
import com.rasel.androidbaseapp.ui.plant_list.PlantListFragment
import com.rasel.androidbaseapp.ui.plant_list.RecyclerViewIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class GalleryFragmentTest {
    private lateinit var navController: TestNavHostController
    val LIST_ITEM_IN_TEST = 0


    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get: Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

//    private val navController: NavController = Mockito.mock(NavController::class.java)

//    private val postFactory = PostFactory()



    @Before
    fun setup() {
        hiltRule.inject()

        //Getting the NavController for test
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        launchFragmentInHiltContainer<PlantListFragment> {

            // Setting the navigation graph for the NavController
            navController.setGraph(R.navigation.nav_graph)

            //Sets the NavigationController for the specified View
            Navigation.setViewNavController(requireView(), navController)

            navController.navigate(R.id.action_nav_home_to_nav_gallery)

        }

        // Register the IdlingResource if you're using it
        IdlingRegistry.getInstance().register(RecyclerViewIdlingResource.getIdlingResource())

    }

    /*@Before
    fun init() {
        val fakeApi = FakeRedditApi()
        fakeApi.addPost(postFactory.createRedditPost(DEFAULT_SUBREDDIT))
        fakeApi.addPost(postFactory.createRedditPost(DEFAULT_SUBREDDIT))
        fakeApi.addPost(postFactory.createRedditPost(DEFAULT_SUBREDDIT))
        val app = ApplicationProvider.getApplicationContext<Application>()
        // use a controlled service locator w/ fake API
        ServiceLocator.swap(
            object : DefaultServiceLocator(app = app, useInMemoryDb = true) {
                override fun getRedditApi(): RedditApi = fakeApi
            }
        )
    }*/
    @Test
    fun showSomeResults() {
        Espresso.onView(withId(R.id.photo_list)).check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as RecyclerView
            assertEquals(3, recyclerView.adapter?.itemCount)
        }
    }
    @After
    fun tearDown() {
        // Unregister the IdlingResource if you're using it
        IdlingRegistry.getInstance().unregister(RecyclerViewIdlingResource.getIdlingResource())
    }
}