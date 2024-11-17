package com.rasel.androidbaseapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rasel.androidbaseapp.base.BaseFragment
import com.rasel.androidbaseapp.databinding.FragmentHomeBinding
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import com.rasel.androidbaseapp.ui.recommended.HomeViewModel
import com.rasel.androidbaseapp.ui.recommended.PhotoListUIModel
import com.rasel.androidbaseapp.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, BaseViewModel>() {

    private lateinit var adapter: HomeRecommendationAdapter

    override val viewModel: HomeViewModel by viewModels()
    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HomeRecommendationAdapter {
            val action = HomeFragmentDirections.actionHomeFragmentToNavPropertyDetails(it)
            findNavController().navigate(action)
        }
        binding.photoList.adapter = adapter

        viewModel.getDataFromUnSplash()
        observe(viewModel.unsplashPhoto, ::onViewStateChange)

        binding.tvSeeAll.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToNavRecommended()
            findNavController().navigate(action)
        }

    }

    private fun onViewStateChange(event: PhotoListUIModel) {
        if (event.isRedelivered) return
        when (event) {
            is PhotoListUIModel.Error -> handleErrorMessage(event.error)
            is PhotoListUIModel.Loading -> handleLoading(binding.progressCircular, true)
            is PhotoListUIModel.Success -> {
                handleLoading(binding.progressCircular, false)
                event.data.let {
                    adapter.submitList(it)
                }
            }
        }
    }

    companion object {
        const val MAX_GRID_SPANS = 3
    }
}