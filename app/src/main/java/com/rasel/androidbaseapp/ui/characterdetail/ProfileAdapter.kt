package com.rasel.androidbaseapp.ui.characterdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rasel.androidbaseapp.ui.plant_list.PlantListFragment
import com.rasel.androidbaseapp.ui.profile.ProfileFragment
import com.rasel.androidbaseapp.ui.settings.SettingsFragment

private const val ARG_OBJECT = "object"

class ProfileAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {

        val fragment = when (position) {
            0 -> ProfileFragment()
            1 -> SettingsFragment()
            else -> PlantListFragment()
        }
        return fragment
    }
}

