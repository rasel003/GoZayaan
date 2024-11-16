package com.rasel.androidbaseapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rasel.androidbaseapp.data.models.RecommendationModel
import com.rasel.androidbaseapp.databinding.ItemRecommendationBinding
import timber.log.Timber
import kotlin.random.Random

/**
 * Adapter for the [RecyclerView] in [UnsplashPhotoListFragment].
 */
class RecommendationAdapter(
    private val spans: Int,
    private var onItemClicked: ((bankData: RecommendationModel) -> Unit)
) : ListAdapter<RecommendationModel, RecyclerView.ViewHolder>(PlantDiffCallback()) {

    /**
     * A [GridLayoutManager.SpanSizeLookup] which randomly assigns a span count to each item
     * in this adapter.
     */
    val variableSpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

        private var indexSpanCounts: List<Int> = emptyList()

        override fun getSpanSize(position: Int): Int {
            val count = if (position< indexSpanCounts.size) indexSpanCounts[position] else 1
            Timber.d("getSpanSize: index : ${indexSpanCounts.size} value : $count : position : $position")
            return count
        }

        private fun generateSpanCountForItems(count: Int): List<Int> {
            val list = mutableListOf<Int>()

            var rowSpansOccupied = 0
            repeat(count) {
                val maxLength = spans + 1 - rowSpansOccupied
                val size = if (maxLength > 1) Random.nextInt(1, maxLength) else 1
                rowSpansOccupied += size
                if (rowSpansOccupied >= RecommendationFragment.MAX_GRID_SPANS) rowSpansOccupied = 0
                list.add(size)
            }
            return list
        }

        override fun invalidateSpanIndexCache() {
            Timber.d("invalidateSpanIndexCache called")
            super.invalidateSpanIndexCache()
            indexSpanCounts = generateSpanCountForItems(itemCount)
        }
    }

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
            binding.setClickListener {
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

private class PlantDiffCallback : DiffUtil.ItemCallback<RecommendationModel>() {

    override fun areItemsTheSame(oldItem: RecommendationModel, newItem: RecommendationModel): Boolean {
        return oldItem.propertyName == newItem.propertyName
    }

    override fun areContentsTheSame(oldItem: RecommendationModel, newItem: RecommendationModel): Boolean {
        return oldItem == newItem
    }
}
