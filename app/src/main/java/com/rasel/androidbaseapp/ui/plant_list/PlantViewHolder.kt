package com.rasel.androidbaseapp.ui.plant_list

import androidx.recyclerview.widget.RecyclerView
import com.rasel.androidbaseapp.cache.entities.Plant
import com.rasel.androidbaseapp.databinding.ListItemPlantBinding

class PlantViewHolder(
    private val binding: ListItemPlantBinding,
    private var onItemClicked: ((plant: Plant) -> Unit),
    private var onBookmarkClicked: ((plant: Plant, position: Int) -> Unit)
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.setClickListener {
            binding.plant?.let { plant ->
                onItemClicked(plant)
            }
        }
        binding.checkBoxBookmark.setOnClickListener {
            binding.plant?.let { plant ->
                onBookmarkClicked(plant, bindingAdapterPosition)
            }
        }
    }

    fun bind(item: Plant) {
        binding.apply {
            plant = item
            executePendingBindings()
        }
    }

    internal fun bindCommentsCount(commentsCount: String) {
        binding.plantItemCommentCount.text = commentsCount
    }

    internal fun bindBookmarkState(bookmarked: Boolean) {
        binding.checkBoxBookmark.isSelected = bookmarked
    }

}
