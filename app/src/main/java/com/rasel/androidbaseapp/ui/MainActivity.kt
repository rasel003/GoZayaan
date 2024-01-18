package com.rasel.androidbaseapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.cache.preferences.PreferenceProvider
import com.rasel.androidbaseapp.core.theme.ThemeUtils
import com.rasel.androidbaseapp.databinding.ActivityMainBinding
import com.rasel.androidbaseapp.presentation.viewmodel.LocalizedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel: LocalizedViewModel by viewModels()

    @Inject
    lateinit var themeUtils: ThemeUtils

    @Inject
    lateinit var preferencesHelper: PreferenceProvider


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themeUtils.setNightMode(preferencesHelper.isNightMode)

        // binding = ActivityMainBinding.inflate(layoutInflater)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main) as ActivityMainBinding
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
//        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
//        val navView: NavigationView = findViewById(R.id.nav_view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow,
                R.id.nav_plantListFragment,
                R.id.nav_character_list,
                R.id.nav_settings
            ), binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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
}