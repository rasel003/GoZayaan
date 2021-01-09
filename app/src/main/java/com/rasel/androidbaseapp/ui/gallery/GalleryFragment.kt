package com.rasel.androidbaseapp.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.rasel.androidbaseapp.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private val adapter = GalleryAdapter()
    private val args: GalleryFragmentArgs by navArgs()
    private var searchJob: Job? = null
    private val viewModel: GalleryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGalleryBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.photoList.adapter = adapter
        search(args.plantName)

        return binding.root
    }

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchPictures(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }
}
