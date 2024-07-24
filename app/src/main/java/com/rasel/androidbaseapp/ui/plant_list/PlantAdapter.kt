package com.rasel.androidbaseapp.ui.plant_list

import android.view.LayoutInflater
import android.view.ViewGroup
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
            ),
            onItemClicked = onItemClicked,
            onBookmarkClicked = onBookmarkClicked
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

}


sealed interface PlantChangePayload {

    data class Comments(val newCommentsCount: String) : PlantChangePayload

    data class Bookmark(val bookmarked: Boolean) : PlantChangePayload
}
