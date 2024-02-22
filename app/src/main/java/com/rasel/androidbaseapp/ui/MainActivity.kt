package com.rasel.androidbaseapp.ui

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.databinding.ActivityMain2Binding
import com.rasel.androidbaseapp.presentation.viewmodel.LocalizedViewModel
import com.rasel.androidbaseapp.util.NetworkChangeReceiver
import com.rasel.androidbaseapp.util.NoticeDialogFragment
import com.rasel.androidbaseapp.util.contentView
import com.rasel.androidbaseapp.util.toastSuccess
import com.rasel.androidbaseapp.util.updateForTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NoticeDialogFragment.NoticeDialogListener,
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
            /*lifecycleScope.launchWhenResumed {
                navController.addOnDestinationChangedListener { _, destination, _ ->
                    when (destination.id) {
                        R.id.nav_slideshow,
                        R.id.nav_plantListFragment,
                        R.id.nav_character_list,
                        R.id.nav_email_list,
                        R.id.nav_settings -> bottomNav.show()
                        else -> bottomNav.hide()
                    }
                }
            }*/
        }

        mContext = this

        mainActivityViewModel.theme.observe(this, Observer(::updateForTheme))

        //network change callback to track network state
        networkChangeReceiver = NetworkChangeReceiver()
        networkChangeReceiver.setConnectionChangeCallback(this)
    }

    override fun onStart() {
        super.onStart()
        mContext.registerReceiver(
            networkChangeReceiver,
            IntentFilter(NetworkChangeReceiver.ACTION_CONNECTIVITY_CHANGE)
        )

        viewModel.getLocalizationFromRemote()
    }

    override fun onStop() {
        super.onStop()
        mContext.unregisterReceiver(networkChangeReceiver)
    }

    fun showNoticeDialog() {
        // Create an instance of the dialog fragment and show it.
        val dialog = NoticeDialogFragment()
        dialog.show(supportFragmentManager, "NoticeDialogFragment")
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following
    // methods defined by the NoticeDialogFragment.NoticeDialogListener
    // interface.
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        // User taps the dialog's positive button.
        /*Snackbar.make(binding.fab, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()*/
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        // User taps the dialog's negative button.
        /*Snackbar.make(binding.fab, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()*/
    }

    override fun onConnectionChange(isConnected: Boolean) {
        mContext.toastSuccess("Network isConnected : $isConnected")
    }
}