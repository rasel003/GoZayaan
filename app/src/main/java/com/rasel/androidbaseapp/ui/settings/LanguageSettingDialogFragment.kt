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

package com.rasel.androidbaseapp.ui.settings

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.data.models.Languages
import com.rasel.androidbaseapp.data.models.Theme
import com.rasel.androidbaseapp.presentation.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageSettingDialogFragment : AppCompatDialogFragment() {

    private val viewModel: SettingsViewModel by viewModels()

    private lateinit var listAdapter: ArrayAdapter<LanguageHolder>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        listAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_single_choice)

        return MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.settings_language_title)
                .setSingleChoiceItems(listAdapter, 0) { dialog, position ->
                    listAdapter.getItem(position)?.theme?.let {
//                        viewModel.setLanguage(it)
                    }
                    dialog.dismiss()
                }
                .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Note you don't need to use viewLifecycleOwner in DialogFragment.
        viewModel.availableLanguage.observe(this) { themes ->
            listAdapter.clear()
            listAdapter.addAll(themes.map { theme -> LanguageHolder(theme, getTitleForLanguage(theme)) })

//            updateSelectedItem(viewModel.theme.value)
        }

//        viewModel.theme.observe(this, Observer(::updateSelectedItem))
    }

    private fun updateSelectedItem(selected: Languages?) {
        val selectedPosition = (0 until listAdapter.count).indexOfFirst { index ->
            listAdapter.getItem(index)?.theme == selected
        }
        (dialog as AlertDialog).listView.setItemChecked(selectedPosition, true)
    }

    private fun getTitleForLanguage(theme: Languages) = when (theme) {
        Languages.ENGLISH -> Languages.ENGLISH.name
        Languages.CHINESE -> Languages.CHINESE.name
        Languages.BURMESE -> Languages.BURMESE.name
    }

    companion object {
        fun newInstance() = LanguageSettingDialogFragment()
    }

    private data class LanguageHolder(val theme: Languages, val title: String) {
        override fun toString(): String = title
    }
}
