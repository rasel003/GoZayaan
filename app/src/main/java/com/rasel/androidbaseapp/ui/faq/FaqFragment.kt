/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rasel.androidbaseapp.ui.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePaddingRelative
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.base.BaseFragment
import com.rasel.androidbaseapp.databinding.FragmentInfoFaqBinding
import com.rasel.androidbaseapp.databinding.FragmentSettingsBinding
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import com.rasel.androidbaseapp.presentation.viewmodel.SettingsViewModel
import com.rasel.androidbaseapp.ui.gallery.SearchRequestViewModel
import com.rasel.androidbaseapp.ui.nav.NavigationModel
import com.rasel.androidbaseapp.ui.settings.SettingsFragment.Companion.MY_KEY
import com.rasel.androidbaseapp.util.doOnApplyWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FaqFragment : BaseFragment<FragmentInfoFaqBinding, BaseViewModel>() {

    override val viewModel: SettingsViewModel by viewModels()
    private val searchRequestViewModel: SearchRequestViewModel by activityViewModels()


    override fun getViewBinding(): FragmentInfoFaqBinding =
        FragmentInfoFaqBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pad the bottom of the ScrollView so that it scrolls up above the nav bar
        view.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePaddingRelative(
                top = padding.top + insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                bottom = padding.bottom + insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            )
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            nonInboxOnBackCallback
        )

        binding.btnContinue.setOnClickListener {
            Timber.tag("rsl").d("fag : ${searchRequestViewModel.cloneSearchQuery.value}")

            searchRequestViewModel.cloneSearchQuery.value =
                searchRequestViewModel.cloneSearchQuery.value?.copy(search = binding.input.text.toString())

            Timber.tag("rsl").d("fag : ${searchRequestViewModel.cloneSearchQuery.value}")


            setResultAndFinish(isResultSet = true)
        }
    }

    // An on back pressed callback that handles replacing any non-Inbox HomeFragment with inbox
    // on back pressed.
    private val nonInboxOnBackCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Timber.tag("rsl").d("handleOnBackPressed : ")

            setResultAndFinish(isResultSet = false)
        }
    }

    private fun setResultAndFinish(isResultSet: Boolean) {
        findNavController().apply {
            previousBackStackEntry?.savedStateHandle?.set(MY_KEY, isResultSet)
            popBackStack()
        }
    }
}
