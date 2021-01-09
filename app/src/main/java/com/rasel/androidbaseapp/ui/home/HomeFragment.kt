package com.rasel.androidbaseapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rasel.androidbaseapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var adapter: HomeAdapter
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

     override fun onCreateView(
             inflater: LayoutInflater,
             container: ViewGroup?,
             savedInstanceState: Bundle?
     ): View {
         binding = FragmentHomeBinding.inflate(inflater, container, false)
         return binding.root
     }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDataFromUnSplash()

        adapter = HomeAdapter()
        binding.photoList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        viewModel.unsplashPhoto.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }
}