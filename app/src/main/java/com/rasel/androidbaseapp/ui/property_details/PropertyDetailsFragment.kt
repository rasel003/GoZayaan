package com.rasel.androidbaseapp.ui.property_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.databinding.FragmentPlantDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A fragment representing a single Plant detail screen.
 */
@AndroidEntryPoint
class PropertyDetailsFragment : Fragment(R.layout.fragment_plant_detail) {

    @Inject
    lateinit var imageLoader: RequestManager
    private val args: PropertyDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentPlantDetailBinding
    private val propertyDetailsVM: PropertyDetailsVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPlantDetailBinding.bind(view).apply {
            item = args.model
        }
    }
}
