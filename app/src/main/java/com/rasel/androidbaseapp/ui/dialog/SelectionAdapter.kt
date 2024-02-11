package com.rasel.androidbaseapp.ui.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.databinding.ItemSearchBinding

class SelectionAdapter(private val listener: CustomClickListener) : ListAdapter<BankData, SelectionAdapter.ViewHolder>(
        DIFF_CALLBACK
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemSearchBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_search,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = getItem(position)
        holder.binding.textView.text = dataModel!!.bankTitle
        holder.binding.root.setOnClickListener { listener.addItem(dataModel) }
        Glide.with(holder.binding.imgPicture).load("https://goo.gl/gEgYUd")
            .placeholder(R.drawable.ic_avatar_placeholder)
            .centerCrop()
            .into(holder.binding.imgPicture)
    }

    class ViewHolder(var binding: ItemSearchBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    interface CustomClickListener {
        fun addItem(bankData: BankData?)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<BankData> =
            object : DiffUtil.ItemCallback<BankData>() {
                override fun areItemsTheSame(oldItem: BankData, newItem: BankData): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: BankData, newItem: BankData): Boolean {
                    return oldItem.bankId == oldItem.bankId
                }
            }
    }
}
