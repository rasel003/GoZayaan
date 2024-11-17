package com.rasel.gozayaan.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rasel.gozayaan.databinding.FragmentHomeBinding
import com.rasel.gozayaan.base.BaseFragment
import com.rasel.gozayaan.presentation.viewmodel.BaseViewModel
import com.rasel.gozayaan.presentation.viewmodel.CoroutinesErrorHandler
import com.rasel.gozayaan.ui.recommended.HomeViewModel
import com.rasel.gozayaan.util.ApiResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        binding.rvRecommended.adapter = adapter

        viewModel.getRecommendationList(object : CoroutinesErrorHandler {
            override fun onError(message: String) {
//                binding.tvError.text = "Error! $message"
            }
        })
        observeRecommendedData()

        binding.tvSeeAll.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToNavRecommended()
            findNavController().navigate(action)
        }

    }

    private fun observeRecommendedData(){
        viewModel.recommendedList.observe(viewLifecycleOwner) {
            binding.progress = it is ApiResponse.Loading
            when (it) {

                is ApiResponse.Success -> {
                    lifecycleScope.launch {
                        binding.progressBar.visibility = View.GONE

                        if (it.data.isNotEmpty()) {
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter.submitList(it.data)

                        } else {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                        }
                    }
                }

                is ApiResponse.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    /* if (it.errorCode == 401) {
                         sessionOut()
                     } else {
                         it.errorBody?.let { it1 -> toastError(it1) }

                     }*/
                }

                else -> {}
            }
        }
    }
}