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
package com.rasel.androidbaseapp.ui.image_slider

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.base.BaseFragment
import com.rasel.androidbaseapp.databinding.FragmentGridBinding
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import com.rasel.androidbaseapp.presentation.viewmodel.CharacterDetailViewModel
import com.rasel.androidbaseapp.ui.MainActivity.Companion.currentPosition
import com.rasel.androidbaseapp.ui.image_slider.adapter.GridAdapter
import com.rasel.androidbaseapp.util.FakeValueFactory
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment for displaying a grid of images.
 */
@AndroidEntryPoint
class GridFragment : BaseFragment<FragmentGridBinding, BaseViewModel>() {

    override fun getViewBinding(): FragmentGridBinding = FragmentGridBinding.inflate(layoutInflater)

    private lateinit var adapter: GridAdapter
    override val viewModel: CharacterDetailViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGridBinding.inflate(inflater, container, false)
//        recyclerView = inflater.inflate(R.layout.fragment_grid, container, false) as RecyclerView
        adapter = GridAdapter(this) { transitionView: View, transitionName: String ->
            findNavController().navigate(
                R.id.action_gridFragment_to_ImagePagerFragment,
                null,
                null,
                FragmentNavigatorExtras(transitionView to transitionName)
            )
        }
        binding.recyclerView.setAdapter(adapter)
        prepareTransitions()
        postponeEnterTransition()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = FakeValueFactory.getImageList(isRandom = false)
        adapter.submitList(data)
        scrollToPosition()
    }

    /**
     * Scrolls the recycler view to show the last viewed item in the grid. This is important when
     * navigating back from the grid.
     */
    private fun scrollToPosition() {
        binding.recyclerView.addOnLayoutChangeListener(object : OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                binding.recyclerView.removeOnLayoutChangeListener(this)
                val layoutManager = binding.recyclerView.layoutManager
                val viewAtPosition = layoutManager!!.findViewByPosition(currentPosition)
                // Scroll to position if the view for the current position is null (not currently part of
                // layout manager children), or it's not completely visible.
                if (viewAtPosition == null || layoutManager
                        .isViewPartiallyVisible(viewAtPosition, false, true)
                ) {
                    binding.recyclerView.post { layoutManager.scrollToPosition(currentPosition) }
                }
            }
        })
    }

    /**
     * Prepares the shared element transition to the pager fragment, as well as the other transitions
     * that affect the flow.
     */
    private fun prepareTransitions() {
        exitTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.grid_exit_transition)

        // A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
        setExitSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: List<String>,
                    sharedElements: MutableMap<String, View>
                ) {
                    // Locate the ViewHolder for the clicked position.
                    val selectedViewHolder =
                        binding.recyclerView.findViewHolderForAdapterPosition(currentPosition)
                            ?: return

                    // Map the first shared element name to the child ImageView.
                    sharedElements[names[0]] =
                        selectedViewHolder.itemView.findViewById(R.id.card_image)
                }
            })
    }
}
