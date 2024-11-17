package com.rasel.androidbaseapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rasel.androidbaseapp.data.models.RecommendationModel
import com.rasel.androidbaseapp.databinding.ItemHomeRecommendationBinding
import com.rasel.androidbaseapp.ui.comparator.PlaceComparator

/**
 * Adapter for the [RecyclerView] in [UnsplashPhotoListFragment].
 */
class HomeRecommendationAdapter(
    private var onItemClicked: (bankData: RecommendationModel) -> Unit
) : ListAdapter<RecommendationModel, RecyclerView.ViewHolder>(PlaceComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlantViewHolder(
            ItemHomeRecommendationBinding.inflate(
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

   inner class PlantViewHolder(
        private val binding: ItemHomeRecommendationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.rootLayout.setOnClickListener {
                binding.photo?.let { plant ->
                    onItemClicked(plant)
                }
            }
        }

       fun bind(item: RecommendationModel) {
            binding.apply {
                photo = item
                executePendingBindings()
            }
        }
    }
}