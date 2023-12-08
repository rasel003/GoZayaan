package com.rasel.androidbaseapp.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.data.network.Resource
import com.rasel.androidbaseapp.databinding.FragmentSlideshowBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SlideshowFragment : Fragment(R.layout.fragment_slideshow) {

    private lateinit var binding: FragmentSlideshowBinding
    private lateinit var slideshowViewModel: SlideshowViewModel
    lateinit var adapter: NotificationAdapter


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        slideshowViewModel = ViewModelProvider(this)[SlideshowViewModel::class.java]
        binding = FragmentSlideshowBinding.inflate(inflater, container, false)

        adapter = NotificationAdapter(ArrayList(), onEditOrderClicked = {

        })
        binding.recyclerview.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        slideshowViewModel.notificationResponse.observe(viewLifecycleOwner) {
            binding.progress = it is Resource.Loading
            when (it) {

                is Resource.Success -> {
                    lifecycleScope.launch {
                        binding.progressBar.visibility = View.GONE

                        if (it.value.records.isNotEmpty()) {
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter.addNewData(it.value.records)

                        } else {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                        }
                    }
                }
                is Resource.Failure -> {
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