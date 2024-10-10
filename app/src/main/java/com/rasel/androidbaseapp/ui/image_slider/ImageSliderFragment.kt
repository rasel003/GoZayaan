package com.rasel.androidbaseapp.ui.image_slider

import android.os.Bundle
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePaddingRelative
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearSnapHelper
import com.bumptech.glide.RequestManager
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.databinding.FragmentImageSliderBinding
/*import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations*/
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.rasel.androidbaseapp.ui.image_slider.MyAdapter.MyItem
import com.rasel.androidbaseapp.util.doOnApplyWindowInsets

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

        // Pad the bottom of the ScrollView so that it scrolls up above the nav bar
        view.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePaddingRelative(
                top = padding.top + insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                bottom = padding.bottom + insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            )
        }

        /*val imageSliderAdapter = ImageSliderAdapter(glide)
        imageSliderAdapter.addItem("https://cdn.paperfly.com.bd/merchant/assets/merchant_dashboard_slide_1.png")
        imageSliderAdapter.addItem("https://cdn.paperfly.com.bd/merchant/assets/mobile_merchant_dashboard_slide_1.png")
        imageSliderAdapter.addItem("https://cdn.paperfly.com.bd/merchant/assets/merchant_dashboard_slide_1.png")
        imageSliderAdapter.addItem("https://cdn.paperfly.com.bd/merchant/assets/mobile_merchant_dashboard_slide_1.png")
        imageSliderAdapter.addItem("https://cdn.paperfly.com.bd/merchant/assets/merchant_dashboard_slide_1.png")
        imageSliderAdapter.addItem("https://cdn.paperfly.com.bd/merchant/assets/mobile_merchant_dashboard_slide_1.png")
        imageSliderAdapter.addItem("https://cdn.paperfly.com.bd/merchant/assets/merchant_dashboard_slide_1.png")*/

        /*binding.sliderView.setSliderAdapter(imageSliderAdapter)
        binding.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.sliderView.startAutoCycle()

        binding.sliderView.setInfiniteAdapterEnabled(true)*/

        // RecyclerView
        val adapter = MyAdapter(glide)
        binding.list.adapter = adapter
        LinearSnapHelper().attachToRecyclerView(binding.list)
        adapter.swapData(LIST_ITEMS)
        binding.pageIndicator attachTo binding.list
        binding.pageIndicator2 attachTo binding.list

        // ViewPager
        val myPagerAdapter = MyPagerAdapter(glide, LIST_ITEMS)
        binding.pager.adapter = myPagerAdapter
        binding.pagerPageIndicator attachTo binding.pager

        /*binding.leftBtn.setOnClickListener {
            binding.pager.arrowScroll(View.FOCUS_LEFT)
        }
        binding.rightBtn.setOnClickListener {
            binding.pager.arrowScroll(View.FOCUS_RIGHT)
        }*/

        // Manual
        binding.manualPageIndicator.count = 50
        binding.leftBtn.setOnClickListener { binding.manualPageIndicator.swipePrevious() }
        binding.rightBtn.setOnClickListener { binding.manualPageIndicator.swipeNext() }
    }

    companion object {
        private val LIST_ITEMS = listOf(
            MyItem(
                "Cormorant fishing at sunset",
                "Patryk Wojciechowicz",
                "https://cdn.dribbble.com/users/3178178/screenshots/6287074/cormorant_fishing_1600x1200_final_04_05_2019_4x.jpg"
            ),
            MyItem(
                "Mountain House",
                "Alex Pasquarella",
                "https://cdn.dribbble.com/users/989466/screenshots/6100954/cabin-2-dribbble-alex-pasquarella_4x.png"
            ),
            MyItem(
                "journey",
                "Febin_Raj",
                "https://cdn.dribbble.com/users/1803663/screenshots/6163551/nature-4_4x.png"
            ),
            MyItem(
                "Explorer",
                "Uran",
                "https://cdn.dribbble.com/users/1355613/screenshots/6441984/landscape_4x.jpg"
            ),
            MyItem(
                "Fishers Peak Limited Edition Print",
                "Brian Edward Miller ",
                "https://cdn.dribbble.com/users/329207/screenshots/6128300/bemocs_fisherspeak_dribbble.jpg"
            ),
            MyItem(
                "First Man",
                "Lana Marandina",
                "https://cdn.dribbble.com/users/1461762/screenshots/6280906/first_man_lana_marandina_4x.png"
            ),
            MyItem(
                "On The Road Again",
                "Brian Edward Miller",
                "https://cdn.dribbble.com/users/329207/screenshots/6522800/2026_nationwide_02_train_landscape_v01.00.jpg"
            )
        )
    }

}
