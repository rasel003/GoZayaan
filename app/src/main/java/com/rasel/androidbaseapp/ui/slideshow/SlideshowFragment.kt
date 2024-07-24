package com.rasel.androidbaseapp.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.data.models.Localization
import com.rasel.androidbaseapp.databinding.FragmentSlideshowBinding
import com.rasel.androidbaseapp.presentation.viewmodel.CoroutinesErrorHandler
import com.rasel.androidbaseapp.presentation.viewmodel.LocalizedViewModel
import com.rasel.androidbaseapp.remote.utils.Resource
import com.rasel.androidbaseapp.util.ApiResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SlideshowFragment : Fragment(R.layout.fragment_slideshow) {

    private lateinit var binding: FragmentSlideshowBinding
    private lateinit var slideshowViewModel: SlideshowViewModel
    private val viewModel: LocalizedViewModel by activityViewModels()
    lateinit var adapter: SlideshowAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        slideshowViewModel = ViewModelProvider(this)[SlideshowViewModel::class.java]
        binding = FragmentSlideshowBinding.inflate(inflater, container, false)

        adapter = SlideshowAdapter(ArrayList(), onEditOrderClicked = {

        })
        binding.recyclerview.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.localizationFlow.asLiveData().observe(viewLifecycleOwner) {

            binding.textView.text = Localization.getTemplatedString(
                it.lblSelectedLanguage,
                viewModel.currentLanguageFlow.value
            )
        }
        slideshowViewModel.getPostList(object : CoroutinesErrorHandler {
            override fun onError(message: String) {
//                binding.tvError.text = "Error! $message"
            }
        })
        slideshowViewModel.postList.observe(viewLifecycleOwner) {
            binding.progress = it is ApiResponse.Loading
            when (it) {

                is ApiResponse.Success -> {
                    lifecycleScope.launch {
                        binding.progressBar.visibility = View.GONE

                        if (it.data.isNotEmpty()) {
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter.addNewData(it.data)

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

        binding.fab.setOnClickListener { fab ->
            val popUpClass = PopUpClass()
            popUpClass.showPopupWindow2(fab)
        }

    }
}