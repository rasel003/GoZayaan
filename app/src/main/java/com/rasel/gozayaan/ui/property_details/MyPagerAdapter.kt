package com.rasel.gozayaan.ui.property_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.RequestManager
import com.rasel.gozayaan.R
import com.rasel.gozayaan.databinding.ItemCardBinding

class MyPagerAdapter(
    private val glide: RequestManager,
    private val items: List<String>
) : PagerAdapter() {

    override fun getCount() = items.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val binding =
            ItemCardBinding.inflate(LayoutInflater.from(container.context), container, false)

        val item = items[position]

        glide
            .load(item)
            .placeholder(R.color.card_background)
            .centerCrop()
            .into(binding.image)

        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }
}