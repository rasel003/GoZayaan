package com.rasel.androidbaseapp.ui.characterlist

import android.os.Bundle
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePaddingRelative
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.base.BaseFragment
import com.rasel.androidbaseapp.databinding.FragmentCharacterListBinding
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import com.rasel.androidbaseapp.presentation.viewmodel.CharacterListViewModel
import com.rasel.androidbaseapp.presentation.viewmodel.CharacterUIModel
import com.rasel.androidbaseapp.util.doOnApplyWindowInsets
import com.rasel.androidbaseapp.util.observe
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CharacterListFragment : BaseFragment<FragmentCharacterListBinding, BaseViewModel>() {

    override fun getViewBinding(): FragmentCharacterListBinding =
        FragmentCharacterListBinding.inflate(layoutInflater)

    override val viewModel: CharacterListViewModel by viewModels()

    @Inject
    lateinit var characterAdapter: CharacterAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pad the bottom of the ScrollView so that it scrolls up above the nav bar
        view.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePaddingRelative(
                top = padding.top + insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
//                top = padding.top + insets.systemWindowInsetTop
            )
        }
        val isFavorite = (findNavController().currentDestination?.label == getString(R.string.menu_favorites))
        viewModel.getCharacters(isFavorite)
        observe(viewModel.getCharacters(), ::onViewStateChange)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerViewCharacters.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        characterAdapter.setItemClickListener { character ->
            findNavController().navigate(
                CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment(
                    character.id.toLong()
                )
            )
        }
    }

    private fun onViewStateChange(event: CharacterUIModel) {
        if (event.isRedelivered) return
        when (event) {
            is CharacterUIModel.Error -> handleErrorMessage(event.error)
            is CharacterUIModel.Loading -> handleLoading(true)
            is CharacterUIModel.Success -> {
                handleLoading(false)
                event.data.let {
                    characterAdapter.list = it
                }
            }

            else -> {}
        }
    }

    override fun handleLoading(isLoading: Boolean) {
//        super.handleLoading(isLoading)

        if(isLoading){
            binding.recyclerViewCharacters.visibility = View.GONE
            binding.shimmerFrameLayout.visibility = View.VISIBLE
            binding.shimmerFrameLayout.startShimmerAnimation()
        }else {
            binding.recyclerViewCharacters.visibility = View.VISIBLE
            binding.shimmerFrameLayout.stopShimmerAnimation()
            binding.shimmerFrameLayout.visibility = View.GONE
        }
    }
}
