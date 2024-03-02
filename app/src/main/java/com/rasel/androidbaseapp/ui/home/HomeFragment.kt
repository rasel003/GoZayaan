package com.rasel.androidbaseapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.rasel.androidbaseapp.base.BaseFragment
import com.rasel.androidbaseapp.databinding.FragmentHomeBinding
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import com.rasel.androidbaseapp.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, BaseViewModel>() {

    private lateinit var adapter: HomeAdapter

    override val viewModel: HomeViewModel by viewModels()
    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding = FragmentHomeBinding.bind(view)

        adapter = HomeAdapter()
        binding.photoList.adapter = adapter

        viewModel.getDataFromUnSplash()
        observe(viewModel.unsplashPhoto, ::onViewStateChange)

    }

    override fun onResume() {
        super.onResume()

       /* viewModel.unsplashPhoto.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) binding.progressCircular.visibility =
                View.GONE else binding.progressCircular.visibility = View.VISIBLE
            adapter.submitList(it)
        }*/
    }

    private fun onViewStateChange(event: PhotoListUIModel) {
        if (event.isRedelivered) return
        when (event) {
            is PhotoListUIModel.Error -> handleErrorMessage(event.error)
            is PhotoListUIModel.Loading -> handleLoading(binding.progressCircular, true)
            is PhotoListUIModel.Success -> {
                handleLoading( binding.progressCircular, false)
                event.data.let {
                    adapter.submitList(it.results)
                }
            }
        }
    }
}