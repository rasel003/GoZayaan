package com.rasel.gozayaan.ui.recommended

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rasel.gozayaan.data.models.RecommendationModel
import com.rasel.gozayaan.databinding.ItemRecommendationBinding
import com.rasel.gozayaan.core.comparator.PlaceComparator

/**
 * Adapter for the [RecyclerView] in [UnsplashPhotoListFragment].
 */
class RecommendationAdapter(
    private var onItemClicked: (bankData: RecommendationModel) -> Unit
) : ListAdapter<RecommendationModel, RecyclerView.ViewHolder>(PlaceComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlantViewHolder(
            ItemRecommendationBinding.inflate(
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
        private val binding: ItemRecommendationBinding
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
