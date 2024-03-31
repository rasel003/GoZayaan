package com.rasel.androidbaseapp.ui.image_slider

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.RequestManager
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.databinding.FragmentImageSliderBinding
import com.rasel.androidbaseapp.ui.ImageSliderAdapter
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A fragment representing a single Plant detail screen.
 */
@AndroidEntryPoint
class ImageSliderFragment : Fragment(R.layout.fragment_image_slider) {

    @Inject
    lateinit var glide: RequestManager

    private lateinit var binding: FragmentImageSliderBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentImageSliderBinding.bind(view)

        val adapter = ImageSliderAdapter(glide)
        adapter.addItem("https://cdn.paperfly.com.bd/merchant/assets/merchant_dashboard_slide_1.png")
        adapter.addItem("https://cdn.paperfly.com.bd/merchant/assets/mobile_merchant_dashboard_slide_1.png")
        adapter.addItem("https://cdn.paperfly.com.bd/merchant/assets/merchant_dashboard_slide_1.png")
        adapter.addItem("https://cdn.paperfly.com.bd/merchant/assets/mobile_merchant_dashboard_slide_1.png")
        adapter.addItem("https://cdn.paperfly.com.bd/merchant/assets/merchant_dashboard_slide_1.png")
        adapter.addItem("https://cdn.paperfly.com.bd/merchant/assets/mobile_merchant_dashboard_slide_1.png")
        adapter.addItem("https://cdn.paperfly.com.bd/merchant/assets/merchant_dashboard_slide_1.png")

        binding.sliderView.setSliderAdapter(adapter)
        binding.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.sliderView.startAutoCycle()

        binding.sliderView.setInfiniteAdapterEnabled(true)
    }

}
