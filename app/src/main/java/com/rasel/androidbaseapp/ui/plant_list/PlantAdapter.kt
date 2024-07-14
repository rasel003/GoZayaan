package com.rasel.androidbaseapp.ui.plant_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rasel.androidbaseapp.cache.entities.Plant
import com.rasel.androidbaseapp.databinding.ListItemPlantBinding
import timber.log.Timber

/**
 * Adapter for the [RecyclerView] in [PlantListFragment].
 */
class PlantAdapter(
    private var onItemClicked: ((plant: Plant) -> Unit),
    private var onBookmarkClicked: ((plant: Plant, position: Int) -> Unit),
) : ListAdapter<Plant, RecyclerView.ViewHolder>(PlantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlantViewHolder(
            ListItemPlantBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        Timber.tag("rsl").d("position : $position")

        val plant = getItem(position)
        (holder as PlantViewHolder).bind(plant)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {

        Timber.tag("rsl")
            .d("with payload position: $position payload : ${payloads.lastOrNull().toString()}")

         when (val latestPayload = payloads.lastOrNull()) {
             is PlantChangePayload.Comments -> (holder as PlantViewHolder).bindCommentsCount(
                 latestPayload.newCommentsCount
             )

             is PlantChangePayload.Bookmark -> (holder as PlantViewHolder).bindBookmarkState(
                 latestPayload.bookmarked
             )

             else -> onBindViewHolder(holder, position)
         }

    }

    internal inner class PlantViewHolder(
        private val binding: ListItemPlantBinding
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

        fun bindBookmarkButton(bookmarked: Boolean, onBookmarkClicked: () -> Unit) {
            bindBookmarkState(bookmarked)

        }
    }
}

private class PlantDiffCallback : DiffUtil.ItemCallback<Plant>() {

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

private sealed interface PlantChangePayload {

    data class Comments(val newCommentsCount: String) : PlantChangePayload

    data class Bookmark(val bookmarked: Boolean) : PlantChangePayload
}
