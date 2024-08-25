package com.rasel.androidbaseapp.ui.characterdetail

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.google.android.material.tabs.TabLayoutMediator
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.base.BaseFragment
import com.rasel.androidbaseapp.databinding.FragmentCharacterDetailBinding
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import com.rasel.androidbaseapp.presentation.viewmodel.Bookmark
import com.rasel.androidbaseapp.presentation.viewmodel.CharacterDetailUIModel
import com.rasel.androidbaseapp.presentation.viewmodel.CharacterDetailViewModel
import com.rasel.androidbaseapp.ui.image_slider.GridFragmentDirections
import com.rasel.androidbaseapp.util.FakeValueFactory
import com.rasel.androidbaseapp.util.observe
import com.rasel.androidbaseapp.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CharacterDetailFragment : BaseFragment<FragmentCharacterDetailBinding, BaseViewModel>() {

    override fun getViewBinding(): FragmentCharacterDetailBinding =
        FragmentCharacterDetailBinding.inflate(layoutInflater)

    override val viewModel: CharacterDetailViewModel by viewModels()
    private val args: CharacterDetailFragmentArgs by navArgs()

    @Inject
    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition

//        postponeEnterTransition(2200, TimeUnit.MILLISECONDS)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.getCharacter(), ::onViewStateChange)
        viewModel.getCharacterDetail(args.characterId)
        setUiChangeListeners()

        binding.imageViewCharacter.setOnClickListener {
            val action = GridFragmentDirections.actionGlobalGridFragment()
            findNavController().navigate(action)
        }
        setTabAndViewpager()
    }

    private fun setTabAndViewpager() {
        val adapter = ProfileAdapter(this)
        binding.pager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = "Profile"
        }.attach()
    }


    private fun setUiChangeListeners() {
        binding.checkBoxBookmark.setOnCheckedChangeListener { view, isChecked ->
            if (!binding.checkBoxBookmark.isPressed) {
                return@setOnCheckedChangeListener
            }
            if (isChecked)
                viewModel.setBookmarkCharacter(view.tag.toString().toLong())
            else
                viewModel.setUnBookmarkCharacter(view.tag.toString().toLong())
        }
    }

    private fun onViewStateChange(result: CharacterDetailUIModel) {
        if (result.isRedelivered) return
        when (result) {
            is CharacterDetailUIModel.BookMarkStatus -> {
                when (result.bookmark) {
                    Bookmark.BOOKMARK ->
                        if (result.status) {
                            showSnackBar(binding.rootView, getString(R.string.bookmark_success))
                        } else {
                            handleErrorMessage(getString(R.string.bookmark_error))
                        }

                    Bookmark.UN_BOOKMARK ->
                        if (result.status) {
                            showSnackBar(
                                binding.rootView,
                                getString(R.string.un_bookmark_success)
                            )
                        } else {
                            handleErrorMessage(getString(R.string.bookmark_error))
                        }
                }
            }

            is CharacterDetailUIModel.Error -> handleErrorMessage(result.error)
            CharacterDetailUIModel.Loading -> handleLoading(true)
            is CharacterDetailUIModel.Success -> {
                handleLoading(false)
                result.data.let { character ->
                    binding.apply {
                        binding.textViewCharacterName.text = character.name
                        glide.load(FakeValueFactory.getImageList(1)[0].title)
                            .into(imageViewCharacter)
                        checkBoxBookmark.tag = character.id
                        checkBoxBookmark.isChecked = character.isBookMarked
                    }
                }
            }
        }
    }
}
