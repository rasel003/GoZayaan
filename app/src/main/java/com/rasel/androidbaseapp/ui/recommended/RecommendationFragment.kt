package com.rasel.androidbaseapp.ui.recommended

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rasel.androidbaseapp.base.BaseFragment
import com.rasel.androidbaseapp.databinding.FragmentRecommendationBinding
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import com.rasel.androidbaseapp.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendationFragment : BaseFragment<FragmentRecommendationBinding, BaseViewModel>() {

    private lateinit var adapter: RecommendationAdapter

    override val viewModel: HomeViewModel by viewModels()
    override fun getViewBinding(): FragmentRecommendationBinding = FragmentRecommendationBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecommendationAdapter(MAX_GRID_SPANS) {
            val action = RecommendationFragmentDirections.actionNavHomeToNavGallery(it)
            findNavController().navigate(action)
        }
        binding.photoList.adapter = adapter

        viewModel.getDataFromUnSplash()
        observe(viewModel.unsplashPhoto, ::onViewStateChange)

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