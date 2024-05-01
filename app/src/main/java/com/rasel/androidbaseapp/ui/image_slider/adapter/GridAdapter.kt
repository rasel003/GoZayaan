/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rasel.androidbaseapp.ui.image_slider.adapter

import android.graphics.drawable.Drawable
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.data.models.TitleAndId
import com.rasel.androidbaseapp.data.models.UnsplashPhoto
import com.rasel.androidbaseapp.databinding.ImageCardBinding
import com.rasel.androidbaseapp.ui.MainActivity.Companion.currentPosition
import com.rasel.androidbaseapp.ui.image_slider.ImagePagerFragment
import com.rasel.androidbaseapp.ui.image_slider.adapter.GridAdapter.ImageViewHolder
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A fragment for displaying a grid of images.
 */
class GridAdapter(
    fragment: Fragment,
    private val onItemSelected: (transitionView: View, transitionName: String) -> Unit
) : ListAdapter<TitleAndId, ImageViewHolder>(TitleAndIdDiffCallback()) {


    /**
     * A listener that is attached to all ViewHolders to handle image loading events and clicks.
     */
    interface ViewHolderListener {
        fun onLoadCompleted(view: ImageView?, adapterPosition: Int)
        fun onItemClicked(view: View, adapterPosition: Int)
    }

    private val requestManager: RequestManager
    private val viewHolderListener: ViewHolderListener

    /**
     * Constructs a new grid adapter for the given [Fragment].
     */
    init {
        requestManager = Glide.with(fragment)
        viewHolderListener = ViewHolderListenerImpl(fragment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ImageCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding, requestManager, viewHolderListener)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind()
    }

    /**
     * Default [ViewHolderListener] implementation.
     */
    inner class ViewHolderListenerImpl(private val fragment: Fragment) :
        ViewHolderListener {
        private val enterTransitionStarted: AtomicBoolean = AtomicBoolean()

        override fun onLoadCompleted(view: ImageView?, position: Int) {
            // Call startPostponedEnterTransition only when the 'selected' image loading is completed.
            if (currentPosition != position) {
                return
            }
            if (enterTransitionStarted.getAndSet(true)) {
                return
            }
            fragment.startPostponedEnterTransition()
        }

        /**
         * Handles a view click by setting the current position to the given `position` and
         * starting a [ImagePagerFragment] which displays the image at the position.
         *
         * @param view the clicked [ImageView] (the shared element view will be re-mapped at the
         * GridFragment's SharedElementCallback)
         * @param position the selected view position
         */
        override fun onItemClicked(view: View, position: Int) {
            // Update the position.
            currentPosition = position

            // ViewCompat.setTransitionName(view, view.context.getString(R.string.transition_image))

            // Exclude the clicked card from the exit transition (e.g. the card will disappear immediately
            // instead of fading out with the rest to prevent an overlapping animation of fade and move).
            (fragment.getExitTransition() as TransitionSet?)!!.excludeTarget(view, true)
            val transitioningView = view.findViewById<ImageView>(R.id.card_image)


            onItemSelected.invoke(transitioningView, transitioningView.transitionName)


            /* fragment.fragmentManager
                 .beginTransaction()
                 .setReorderingAllowed(true) // Optimize for shared element transition
                 .addSharedElement(transitioningView, transitioningView.transitionName)
                 .replace(
                     R.id.fragment_container, ImagePagerFragment(), ImagePagerFragment::class.java
                         .simpleName
                 )
                 .addToBackStack(null)
                 .commit()*/
        }
    }

    /**
     * ViewHolder for the grid's images.
     */
    inner class ImageViewHolder(
        private val binding: ImageCardBinding, private val requestManager: RequestManager,
        private val viewHolderListener: ViewHolderListener
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.cardView.setOnClickListener(this)
        }

        /**
         * Binds this view holder to the given adapter position.
         *
         * The binding will load the image into the image view, as well as set its transition name for
         * later.
         */
        fun onBind() {
            val adapterPosition = bindingAdapterPosition
            setImage(adapterPosition)
            // Set the string value of the image resource as the unique transition name for the view.
//            binding.cardImage.transitionName = ImageData.IMAGE_DRAWABLES[adapterPosition].toString()
            binding.cardImage.transitionName =
                getItem(adapterPosition).id.toString()
        }

        private fun setImage(adapterPosition: Int) {
            // Load the image with Glide to prevent OOM error when the image drawables are very large.
            requestManager
//                .load(ImageData.IMAGE_DRAWABLES[adapterPosition])
                .load(getItem(adapterPosition).title)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?, model: Any?,
                        target: Target<Drawable?>, isFirstResource: Boolean
                    ): Boolean {
                        viewHolderListener.onLoadCompleted(binding.cardImage, adapterPosition)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable?>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        viewHolderListener.onLoadCompleted(binding.cardImage, adapterPosition)
                        return false
                    }
                })
                .into(binding.cardImage)
        }

        override fun onClick(view: View) {
            // Let the listener start the ImagePagerFragment.
            viewHolderListener.onItemClicked(view, bindingAdapterPosition)
        }
    }
}

private class TitleAndIdDiffCallback : DiffUtil.ItemCallback<TitleAndId>() {

    override fun areItemsTheSame(oldItem: TitleAndId, newItem: TitleAndId): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TitleAndId, newItem: TitleAndId): Boolean {
        return oldItem.title == newItem.title
    }
}