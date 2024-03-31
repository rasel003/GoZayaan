package com.rasel.androidbaseapp.ui.image_slider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.RequestManager
import com.rasel.androidbaseapp.R

class MyPagerAdapter(
    private val glide: RequestManager,
    private val items: List<MyAdapter.MyItem>
) : PagerAdapter() {

    override fun getCount() = items.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater
            .from(container.context)
            .inflate(R.layout.item_card, container, false)

        val item = items[position]
        val title: TextView = view.findViewById(R.id.title)
        val caption: TextView = view.findViewById(R.id.caption)
        val image: ImageView = view.findViewById(R.id.image)

        glide
            .load(item.image)
            .placeholder(R.color.colorPrimaryDark)
            .centerCrop()
            .into(image)

        title.text = item.title
        caption.text = item.caption

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }
}