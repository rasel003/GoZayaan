package com.rasel.androidbaseapp.ui.image_slider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.rasel.androidbaseapp.R

class MyAdapter(
    private val glide: RequestManager
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private val items: MutableList<MyItem> = mutableListOf()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_card, parent, false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(glide, items[holder.adapterPosition])
    }

    fun swapData(data: Iterable<MyItem>?) {
        items.clear()
        data?.let { items.addAll(data) }
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(R.id.title)
        private val caption: TextView = itemView.findViewById(R.id.caption)
        private val image: ImageView = itemView.findViewById(R.id.image)

        fun bind(picasso: RequestManager, item: MyItem) {
            picasso
                .load(item.image)
                .placeholder(R.color.colorPrimaryDark)
                .centerCrop()
                .into(image)
            title.text = item.title
            caption.text = item.caption
        }
    }

    class MyItem(val title: String, val caption: String, val image: String)
}
