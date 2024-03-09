package com.rasel.androidbaseapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rasel.androidbaseapp.data.models.UnsplashPhoto
import com.rasel.androidbaseapp.databinding.DialogPhotoDetailsBinding
import com.rasel.androidbaseapp.databinding.ListItemHomeBinding
import com.rasel.androidbaseapp.databinding.ListItemPhotoBinding
import com.rasel.androidbaseapp.ui.gallery.GalleryFragment
import timber.log.Timber
import kotlin.random.Random

/**
 * Adapter for the [RecyclerView] in [UnsplashPhotoListFragment].
 */
class HomeAdapter(
    private val spans: Int
) : ListAdapter<UnsplashPhoto, RecyclerView.ViewHolder>(PlantDiffCallback()) {

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
                if (rowSpansOccupied >= HomeFragment.MAX_GRID_SPANS) rowSpansOccupied = 0
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
            ListItemHomeBinding.inflate(
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
        private val binding: ListItemHomeBinding
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

           val dialog = MaterialAlertDialogBuilder(view.context)

            val bindingDialog = DialogPhotoDetailsBinding.inflate(LayoutInflater.from(view.context))

            Glide.with(view.context)
                .load(plant.urls.small)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(bindingDialog.imageView)

            bindingDialog.tvUserName.text = plant.user.name

            dialog.setView(bindingDialog.root)

            dialog.show()
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
