package com.rasel.androidbaseapp.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.rasel.androidbaseapp.base.BaseFragment
import com.rasel.androidbaseapp.core.theme.ThemeUtils
import com.rasel.androidbaseapp.data.models.Localization
import com.rasel.androidbaseapp.databinding.FragmentSettingsBinding
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import com.rasel.androidbaseapp.presentation.viewmodel.SettingUIModel
import com.rasel.androidbaseapp.presentation.viewmodel.SettingsViewModel
import com.rasel.androidbaseapp.util.OrderUpdateHistoryMerchantDialog
import com.rasel.androidbaseapp.util.observe
import com.rasel.androidbaseapp.presentation.viewmodel.LocalizedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, BaseViewModel>() {

    override val viewModel: SettingsViewModel by viewModels()
    private val localizedViewModel: LocalizedViewModel by activityViewModels()

    @Inject
    lateinit var settingsAdapter: SettingsAdapter

    @Inject
    lateinit var themeUtils: ThemeUtils

    override fun getViewBinding(): FragmentSettingsBinding = FragmentSettingsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        localizedViewModel.localizationFlow.asLiveData().observe(viewLifecycleOwner){
            binding.btnEnglish.text = it.lblEnglish
            binding.btnBurmese.text = it.lblBurmese
            binding.btnChinese.text = it.lblChinese

            binding.textView.text =
                Localization.getTemplatedString(it.lblSelectedLanguage,
                    localizedViewModel.currentLanguageFlow.value
                )

        }
        binding.btnEnglish.setOnClickListener{localizedViewModel.switchToEnglish()}
        binding.btnBurmese.setOnClickListener{localizedViewModel.switchToBurmese()}
        binding.btnChinese.setOnClickListener{localizedViewModel.switchToChinese()}

        binding.textView.setOnClickListener {
            OrderUpdateHistoryMerchantDialog.display(
                childFragmentManager, "history",
                "it1"
            )
        }

        observe(viewModel.settings, ::onViewStateChange)
        setupRecyclerView()
        viewModel.getSettings()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewSettings.apply {
            adapter = settingsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        settingsAdapter.setItemClickListener { selectedSetting ->
            viewModel.setSettings(selectedSetting)
        }
    }

    private fun onViewStateChange(result: SettingUIModel) {
        if (result.isRedelivered) return
        when (result) {
            is SettingUIModel.Error -> handleErrorMessage(result.error)
            SettingUIModel.Loading -> handleLoading(true)
            is SettingUIModel.NightMode -> {
                result.nightMode.let {
                    themeUtils.setNightMode(it)
                }
            }
            is SettingUIModel.Success -> {
                handleLoading(false)
                result.data.let {
                    settingsAdapter.list = it
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}