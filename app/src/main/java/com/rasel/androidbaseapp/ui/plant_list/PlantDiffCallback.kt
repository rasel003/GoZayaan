package com.rasel.androidbaseapp.ui.plant_list

import androidx.recyclerview.widget.DiffUtil
import com.rasel.androidbaseapp.cache.entities.Plant

class PlantDiffCallback : DiffUtil.ItemCallback<Plant>() {

    override fun areItemsTheSame(oldItem: Plant, newItem: Plant): Boolean {
        return oldItem.plantId == newItem.plantId
    }

    override fun areContentsTheSame(oldItem: Plant, newItem: Plant): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Plant, newItem: Plant): Any? {

        return when {
            oldItem.commentsCount != newItem.commentsCount -> {
                PlantChangePayload.Comments(newItem.commentsCount)
            }

            oldItem.bookmarked != newItem.bookmarked -> {
                PlantChangePayload.Bookmark(newItem.bookmarked)
            }

            else -> super.getChangePayload(oldItem, newItem)
        }
    }
}
