package com.rasel.androidbaseapp.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.databinding.ActivityMain2Binding
import com.rasel.androidbaseapp.presentation.viewmodel.LocalizedViewModel
import com.rasel.androidbaseapp.util.NetworkChangeReceiver
import com.rasel.androidbaseapp.util.contentView
import com.rasel.androidbaseapp.util.toastSuccess
import com.rasel.androidbaseapp.util.updateForTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    NetworkChangeReceiver.ConnectionChangeCallback {

    private lateinit var mContext: Context
    private val viewModel: LocalizedViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    private val binding: ActivityMain2Binding by contentView(R.layout.activity_main2)

    private lateinit var networkChangeReceiver: NetworkChangeReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Update for Dark Mode straight away
        updateForTheme(mainActivityViewModel.currentTheme)

        binding.apply {
            val navController = Navigation.findNavController(this@MainActivity, R.id.nav_host)
            bottomNav.setupWithNavController(navController)

            /* val appBarConfiguration = AppBarConfiguration(
                 setOf(
                     R.id.nav_home,
                     R.id.nav_gallery,
                     R.id.nav_slideshow,
                     R.id.nav_plantListFragment,
                     R.id.nav_character_list,
                     R.id.nav_email_list,
                     R.id.nav_email_list,
                     R.id.nav_settings
                 ), null
             )

             setSupportActionBar(toolbar)
             setupActionBarWithNavController(navController, appBarConfiguration)
             binding.toolbar.setupWithNavController(navController)*/

            // Hide bottom nav on screens which don't require it
            lifecycleScope.launchWhenResumed {
                navController.addOnDestinationChangedListener { controller: NavController, destination: NavDestination, temp: Bundle? ->
                    /*when (destination.id) {
                        R.id.nav_slideshow,
                        R.id.nav_plantListFragment,
                        R.id.nav_character_list,
                        R.id.nav_email_list,
                        R.id.nav_settings -> bottomNav.show()
                        else -> bottomNav.hide()
                    }*/
                }
            }
        }

        mContext = this

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION, 0)
            // Return here to prevent adding additional GridFragments when changing orientation.
            return
        }

        mainActivityViewModel.theme.observe(this, Observer(::updateForTheme))

        //network change callback to track network state
        networkChangeReceiver = NetworkChangeReceiver()
        networkChangeReceiver.setConnectionChangeCallback(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_POSITION, currentPosition)
    }

    override fun onStart() {
        super.onStart()
       /* mContext.registerReceiver(
            networkChangeReceiver,
            IntentFilter(NetworkChangeReceiver.ACTION_CONNECTIVITY_CHANGE)
        )*/

        viewModel.getLocalizationFromRemote()
    }

    override fun onConnectionChange(isConnected: Boolean) {
        mContext.toastSuccess("Network isConnected : $isConnected")
    }

    companion object {

        /**
         * Holds the current image position to be shared between the grid and the pager fragments. This
         * position updated when a grid item is clicked, or when paging the pager.
         *
         * In this demo app, the position always points to an image index at the [ ] class.
         */
        var currentPosition = 0
        private const val KEY_CURRENT_POSITION =
            "com.google.samples.gridtopager.key.currentPosition"
    }
}