package com.rasel.androidbaseapp.ui.image_slider

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.rasel.androidbaseapp.databinding.ItemImageSliderBinding
import com.rasel.androidbaseapp.ui.image_slider.ImageSliderAdapter.SliderAdapterVH
import com.smarteist.autoimageslider.SliderViewAdapter

class ImageSliderAdapter(
    private val glide: RequestManager
) : SliderViewAdapter<SliderAdapterVH>() {
    private var mSliderItems: MutableList<String> = ArrayList()
    fun renewItems(sliderItems: MutableList<String>) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        mSliderItems.removeAt(position)
        notifyDataSetChanged()
    }

    fun addItem(sliderItem: String) {
        mSliderItems.add(sliderItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        return SliderAdapterVH(
            ItemImageSliderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val sliderItem = mSliderItems[position]
        viewHolder.bind(sliderItem)
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return mSliderItems.size
    }

    inner class SliderAdapterVH(var binding: ItemImageSliderBinding) : ViewHolder(
        binding.root
    ) {
        fun bind(sliderItem: String?) {
            glide.load(sliderItem).into(binding.shapeableImageView)
            //            binding.shapeableImageView.setImageURI(Uri.parse(sliderItem));
        }
    }
}