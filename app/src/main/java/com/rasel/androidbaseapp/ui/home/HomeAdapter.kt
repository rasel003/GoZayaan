package com.rasel.androidbaseapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rasel.androidbaseapp.data.network.model.UnsplashPhoto
import com.rasel.androidbaseapp.databinding.ListItemPhotoBinding

/**
 * Adapter for the [RecyclerView] in [UnsplashPhotoListFragment].
 */
class HomeAdapter : ListAdapter<UnsplashPhoto, RecyclerView.ViewHolder>(PlantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlantViewHolder(
            ListItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val plant = getItem(position)
        (holder as PlantViewHolder).bind(plant)
    }

    class PlantViewHolder(
        private val binding: ListItemPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.photo?.let { plant ->
                    navigateToPlant(plant, it)
                }
            }
        }

        private fun navigateToPlant(
            plant: UnsplashPhoto,
            view: View
        ) {
           /* val direction = PlantListFragmentDirections.actionPlantListFragmentToPlantDetailFragment(plant.plantId)
            view.findNavController().navigate(direction)*/
        }

        fun bind(item: UnsplashPhoto) {
            binding.apply {
                photo = item
                executePendingBindings()
            }
        }
    }
}

private class PlantDiffCallback : DiffUtil.ItemCallback<UnsplashPhoto>() {

    override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
        return oldItem == newItem
    }
}
