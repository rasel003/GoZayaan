package com.rasel.androidbaseapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var adapter: HomeAdapter
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        viewModel.getDataFromUnSplash()

        adapter = HomeAdapter()
        binding.photoList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        viewModel.unsplashPhoto.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) binding.progressCircular.visibility =
                View.GONE else binding.progressCircular.visibility = View.VISIBLE
            adapter.submitList(it)
        })
    }
}