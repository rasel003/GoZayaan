package com.rasel.androidbaseapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.databinding.ActivityMain2Binding
import com.rasel.androidbaseapp.presentation.viewmodel.LocalizedViewModel
import com.rasel.androidbaseapp.util.NoticeDialogFragment
import com.rasel.androidbaseapp.util.updateForTheme
import com.rasel.androidbaseapp.util.contentView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NoticeDialogFragment.NoticeDialogListener {

    private val viewModel: LocalizedViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    private val binding: ActivityMain2Binding by contentView(R.layout.activity_main2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Update for Dark Mode straight away
        updateForTheme(mainActivityViewModel.currentTheme)

        binding.apply {
            val navController = Navigation.findNavController(this@MainActivity, R.id.nav_host)
            bottomNav.setupWithNavController(navController)

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

        mainActivityViewModel.theme.observe(this, Observer(::updateForTheme))


    }

    override fun onStart() {
        super.onStart()
        viewModel.getLocalizationFromRemote()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_logOut -> {
                logOutFromApp(); true
            }

            R.id.action_settings -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logOutFromApp() {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.titleLogOut))
            .setMessage(resources.getString(R.string.messageLogOut))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                // Respond to positive button press
                finish()
            }
            .show()
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
}