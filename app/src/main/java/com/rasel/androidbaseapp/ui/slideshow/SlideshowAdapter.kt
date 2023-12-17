package com.rasel.androidbaseapp.ui.slideshow

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rasel.androidbaseapp.data.network.Records
import com.rasel.androidbaseapp.data.network.responses.PostItem
import com.rasel.androidbaseapp.databinding.ItemNotificationBinding


class SlideshowAdapter(
    var dataArrayList: MutableList<PostItem>,
    private var onEditOrderClicked: ((item: PostItem) -> Unit),
) : RecyclerView.Adapter<SlideshowAdapter.MyViewHOlder>() {

    private var layoutInflater: LayoutInflater? = null
    private val mFilterData: List<PostItem>

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHOlder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.context)
        }
        val binding = ItemNotificationBinding.inflate(
            layoutInflater!!, viewGroup, false
        )
        return MyViewHOlder(binding)
    }

    override fun onBindViewHolder(itemView: MyViewHOlder, position: Int) {
        itemView.bind(dataArrayList[position], position)
    }

    override fun getItemCount(): Int {
        return dataArrayList.size
    }

    fun addNewData(orderHistoryItemList: List<PostItem>) {
        dataArrayList.clear()
        dataArrayList.addAll(orderHistoryItemList)
        notifyDataSetChanged()
    }

    inner class MyViewHOlder(
        private val binding: ItemNotificationBinding,
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(item: PostItem, position: Int) {
            binding.tvTitle.text = item.title
            binding.tvSubTitle.text = item.body
            binding.llNotification.setOnClickListener {
                onEditOrderClicked(item)
            }
        }

    }

    init {
        mFilterData = dataArrayList
    }
}