package com.rasel.androidbaseapp.ui.slideshow

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rasel.androidbaseapp.data.network.Records
import com.rasel.androidbaseapp.databinding.ItemNotificationBinding


class SlideshowAdapter(
    var dataArrayList: MutableList<Records>,
    private var onEditOrderClicked: ((item: Records) -> Unit),
) : RecyclerView.Adapter<SlideshowAdapter.MyViewHOlder>() {

    private var layoutInflater: LayoutInflater? = null
    private val mFilterData: List<Records>

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

    fun addNewData(orderHistoryItemList: List<Records>) {
        dataArrayList.clear()
        dataArrayList.addAll(orderHistoryItemList)
        notifyDataSetChanged()
    }

    inner class MyViewHOlder(
        private val binding: ItemNotificationBinding,
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(item: Records, position: Int) {
            binding.tvTitle.text = item.body
            binding.tvSubTitle.text = item.date_sent
            binding.llNotification.setOnClickListener {
                if (item.date_read == null) {
                    onEditOrderClicked(item)
                }
            }
            if (item.date_read != null) {
                binding.ivArrow.visibility = View.INVISIBLE
                binding.tvTitle.setTextColor(Color.parseColor("#999999"))
            } else {
                binding.ivArrow.visibility = View.VISIBLE
                binding.tvTitle.setTextColor(Color.parseColor("#334660"))
            }
        }

    }

    init {
        mFilterData = dataArrayList
    }
}