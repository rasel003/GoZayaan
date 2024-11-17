package com.rasel.gozayaan.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.rasel.gozayaan.R
import com.rasel.gozayaan.databinding.ActivityMainBinding
import com.rasel.gozayaan.util.NetworkChangeReceiver
import com.rasel.gozayaan.util.contentView
import com.rasel.gozayaan.util.toastSuccess
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    NetworkChangeReceiver.ConnectionChangeCallback {

    private lateinit var mContext: Context

    private val binding: ActivityMainBinding by contentView(R.layout.activity_main)

    private lateinit var networkChangeReceiver: NetworkChangeReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            val navController = Navigation.findNavController(this@MainActivity, R.id.nav_host)
            bottomNav.setupWithNavController(navController)

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

        //network change callback to track network state
        networkChangeReceiver = NetworkChangeReceiver()
        networkChangeReceiver.setConnectionChangeCallback(this)
    }



    override fun onStart() {
        super.onStart()
       /* mContext.registerReceiver(
            networkChangeReceiver,
            IntentFilter(NetworkChangeReceiver.ACTION_CONNECTIVITY_CHANGE)
        )*/

    }

    override fun onConnectionChange(isConnected: Boolean) {
        mContext.toastSuccess("Network isConnected : $isConnected")
    }
}