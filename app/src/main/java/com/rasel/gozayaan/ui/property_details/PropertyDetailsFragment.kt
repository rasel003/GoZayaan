package com.rasel.gozayaan.ui.property_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.rasel.gozayaan.base.BaseFragment
import com.rasel.gozayaan.databinding.FragmentPropertyDetailBinding
import com.rasel.gozayaan.presentation.viewmodel.BaseViewModel
import com.rasel.gozayaan.ui.recommended.HomeViewModel
import com.rasel.gozayaan.util.capitalizeFirstCharacter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A fragment representing a single Plant detail screen.
 */
@AndroidEntryPoint
class PropertyDetailsFragment : BaseFragment<FragmentPropertyDetailBinding, BaseViewModel>() {

    @Inject
    lateinit var imageLoader: RequestManager
    @Inject
    lateinit var glide: RequestManager

    private val args: PropertyDetailsFragmentArgs by navArgs()

    override val viewModel: HomeViewModel by viewModels()
    override fun getViewBinding(): FragmentPropertyDetailBinding = FragmentPropertyDetailBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       val recommendationModel = args.model

        // ViewPager
        val myPagerAdapter = MyPagerAdapter(glide, args.model.detailImages ?: emptyList())
        binding.pager.adapter = myPagerAdapter
        binding.pagerPageIndicator attachTo binding.pager


        binding.tvPlaceName.text = recommendationModel.propertyName
        binding.tvCountryName.text = recommendationModel.location
        binding.tvRattingValue.text = recommendationModel.rating.toString()
        binding.tvTripDescription.text = recommendationModel.description
        binding.tvPrice.text = "$"+recommendationModel.fare
        binding.tvPerDay.text = "/"+recommendationModel.fareUnit?.capitalizeFirstCharacter()

    }
}
