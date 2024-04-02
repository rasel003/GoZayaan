package com.rasel.androidbaseapp.ui.scrolling_tab

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.data.models.TitleInfo
import com.rasel.androidbaseapp.databinding.FragmentWithScrollViewBinding
import com.rasel.androidbaseapp.ui.image_slider.MyAdapter.MyItem
import com.rasel.androidbaseapp.ui.scrolling_tab.adapters.TitlesAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * A fragment representing a single Plant detail screen.
 */
@AndroidEntryPoint
class WithScrollViewFragment : Fragment(R.layout.fragment_with_scroll_view) {

    @Inject
    lateinit var glide: RequestManager

    private lateinit var mContext: Context
    private lateinit var binding: FragmentWithScrollViewBinding

    private var titlesAdapter: TitlesAdapter? = null
    private var selectedTitlePosition: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWithScrollViewBinding.bind(view)

        binding.tvTitles.apply {
            layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            titlesAdapter = TitlesAdapter(mContext, getTitles()) {
                selectedTitlePosition = it
                setTitleSelected()

                when (selectedTitlePosition) {
                    0 -> {
                        binding.sv.scrollTo(0, binding.testDataView0.llType0.top)
                    }

                    1 -> {
                        binding.sv.scrollTo(0, binding.testDataView1.llType1.top)
                    }

                    2 -> {
                        binding.sv.scrollTo(0, binding.testDataView2.llType2.top)
                    }
                }
            }
            adapter = titlesAdapter
        }

        val scrollBounds = Rect()
        binding.sv.getHitRect(scrollBounds)

        binding.sv.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (binding.testDataView0.tvContentTitle1.getLocalVisibleRect(scrollBounds)) {
                if (!binding.testDataView0.tvContentTitle1.getLocalVisibleRect(scrollBounds)
                    || scrollBounds.height() < binding.testDataView0.tvContentTitle1.height
                ) {
                    Timber.tag(TAG).e("tvContentTitle1 displayed partial portion")
                } else {
                    Timber.tag(TAG).e("tvContentTitle1 displayed full portion")
                }
                selectedTitlePosition = 0
                setTitleSelected()

            } else {
                Timber.tag(TAG).e("tvContentTitle1 Not displayed")
            }

            if (binding.testDataView1.tvContentTitle2.getLocalVisibleRect(scrollBounds)) {
                if (!binding.testDataView1.tvContentTitle2.getLocalVisibleRect(scrollBounds)
                    || scrollBounds.height() < binding.testDataView1.tvContentTitle2.height
                ) {
                    Timber.tag(TAG).e("tvContentTitle2 displayed partial portion")
                } else {
                    Timber.tag(TAG).e("tvContentTitle2 displayed full portion")
                }
                selectedTitlePosition = 1
                setTitleSelected()

            } else {
                Timber.tag(TAG).e("tvContentTitle2 Not displayed")
            }

            if (binding.testDataView2.tvContentTitle3.getLocalVisibleRect(scrollBounds)) {
                if (!binding.testDataView2.tvContentTitle3.getLocalVisibleRect(scrollBounds)
                    || scrollBounds.height() < binding.testDataView2.tvContentTitle3.height
                ) {
                    Timber.tag(TAG).e("tvContentTitle3 displayed partial portion")
                } else {
                    Timber.tag(TAG).e("tvContentTitle3 displayed full portion")
                }
                selectedTitlePosition = 2
                setTitleSelected()

            } else {
                Timber.tag(TAG).e("tvContentTitle3 Not displayed")
            }
        })
    }

    private fun getTitles() : ArrayList<TitleInfo?> {
        return ArrayList<TitleInfo?>().apply {
            add(TitleInfo("Classification", true))
            add(TitleInfo("Overview", false))
            add(TitleInfo("Description", false))
        }
    }

    private fun setTitleSelected() {
        titlesAdapter?.updateList(selectedTitlePosition)
    }

    companion object {
        const val TAG = "dfkd"
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
