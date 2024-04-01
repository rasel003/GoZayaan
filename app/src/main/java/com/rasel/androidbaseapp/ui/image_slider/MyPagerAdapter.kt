package com.rasel.androidbaseapp.ui.image_slider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.RequestManager
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.databinding.ItemCardBinding

class MyPagerAdapter(
    private val glide: RequestManager,
    private val items: List<MyAdapter.MyItem>
) : PagerAdapter() {

    override fun getCount() = items.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val binding =
            ItemCardBinding.inflate(LayoutInflater.from(container.context), container, false)

        val item = items[position]

        glide
            .load(item.image)
            .placeholder(R.color.colorPrimaryDark)
            .centerCrop()
            .into(binding.image)

        binding.title.text = item.title
        binding.caption.text = item.caption

        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }
}