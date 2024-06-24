package com.rasel.androidbaseapp.ui.characterdetail

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.transition.TransitionInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.base.BaseFragment
import com.rasel.androidbaseapp.databinding.FragmentCharacterDetailBinding
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import com.rasel.androidbaseapp.presentation.viewmodel.Bookmark
import com.rasel.androidbaseapp.presentation.viewmodel.CharacterDetailUIModel
import com.rasel.androidbaseapp.presentation.viewmodel.CharacterDetailViewModel
import com.rasel.androidbaseapp.ui.image_slider.GridFragmentDirections
import com.rasel.androidbaseapp.util.isResizableNeeded
import com.rasel.androidbaseapp.util.observe
import com.rasel.androidbaseapp.util.setResizableText
import com.rasel.androidbaseapp.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CharacterDetailFragment : BaseFragment<FragmentCharacterDetailBinding, BaseViewModel>() {

    override fun getViewBinding(): FragmentCharacterDetailBinding = FragmentCharacterDetailBinding.inflate(layoutInflater)

    override val viewModel: CharacterDetailViewModel by viewModels()

    private val args: CharacterDetailFragmentArgs by navArgs()

    @Inject
    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition

//        postponeEnterTransition(2200, TimeUnit.MILLISECONDS)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.getCharacter(), ::onViewStateChange)
        viewModel.getCharacterDetail(args.characterId)
        setUiChangeListeners()

        binding.cardViewImage.setOnClickListener {
            val result=binding.tvCharacterDetails.isResizableNeeded(getString(R.string.faq_after_dark_program_description), 4)
            Toast.makeText(it.context, "Resizable $result", Toast.LENGTH_SHORT).show()
        }

        binding.tvCharacterDetails.setResizableText(getString(R.string.faq_after_dark_program_description), 3, true)

        binding.galleryView.setOnClickListener {
            val action = GridFragmentDirections.actionGlobalGridFragment()
            findNavController().navigate(action)
        }

        setBoldText()
    }

    private fun setBoldText(){
        // Find your TextView in the layout

        // The full text
        val fullText = "Mon 3rd Feb, 11 AM"

        // Create a SpannableString from the full text
        val spannableString = SpannableString(fullText)

        // Find the start and end indices of the bold part
        val startIndex = fullText.indexOf(" ")
        val endIndex = fullText.indexOf(",")

        if(startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            // Set the StyleSpan to bold for the specified range
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                startIndex + 1,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // Set the SpannableString to the TextView
        binding.tvCharacterDetails2.text = spannableString
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
                        glide.load(character.image).into(imageViewCharacter)
                        checkBoxBookmark.tag = character.id
                        checkBoxBookmark.isChecked = character.isBookMarked
                        textViewSpecies.text = character.species
                        textViewGender.text = character.gender
                        textViewGenderLocation.text = character.characterLocation.name
                        textViewStatus.text = character.status
                    }
                }
            }
        }
    }
}
