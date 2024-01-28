package com.rasel.androidbaseapp.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.util.Pair
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.rasel.androidbaseapp.R
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
import com.rasel.androidbaseapp.util.getDatePicker
import com.rasel.androidbaseapp.util.getDateRangePicker
import com.rasel.androidbaseapp.util.getStringDateFromTimeInMillis
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, BaseViewModel>() {

    override val viewModel: SettingsViewModel by viewModels()
    private val localizedViewModel: LocalizedViewModel by activityViewModels()

    @Inject
    lateinit var settingsAdapter: SettingsAdapter

    @Inject
    lateinit var themeUtils: ThemeUtils


    private lateinit var dateRangePicker: MaterialDatePicker<Pair<Long, Long>>
    private var endDate: String = ""
    private var startDate: String = ""

    private lateinit var datePicker: MaterialDatePicker<Long>
    private var selectedDate: String = ""

    override fun getViewBinding(): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        localizedViewModel.localizationFlow.asLiveData().observe(viewLifecycleOwner) {
            binding.btnEnglish.text = it.lblEnglish
            binding.btnBurmese.text = it.lblBurmese
            binding.btnChinese.text = it.lblChinese

            binding.textView.text =
                Localization.getTemplatedString(
                    it.lblSelectedLanguage,
                    localizedViewModel.currentLanguageFlow.value
                )

        }
        binding.btnEnglish.setOnClickListener { localizedViewModel.switchToEnglish() }
        binding.btnBurmese.setOnClickListener { localizedViewModel.switchToBurmese() }
        binding.btnChinese.setOnClickListener { localizedViewModel.switchToChinese() }

        binding.chipHome.setOnClickListener {
            findNavController().navigate(R.id.action_global_nav_home)
        }
        binding.chipOwl.setOnClickListener {
            findNavController().navigate(R.id.action_global_owl_onboarding)
        }

        binding.textView.setOnClickListener {
            OrderUpdateHistoryMerchantDialog.display(
                childFragmentManager, "history",
                "it1"
            )
        }
        setDateRangeSelection()
        setDateSelection()

        val adapterOnhold = ArrayAdapter(
            requireContext(),
            R.layout.list_item,
            resources.getStringArray(R.array.hold_reasons_v2)
        )
        adapterOnhold.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        (binding.tilReasonList.editText as? AutoCompleteTextView)?.setAdapter(adapterOnhold)

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

    private fun setDateRangeSelection() {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val yesterday = calendar.timeInMillis
        dateRangePicker = getDateRangePicker()
            .setSelection(
                Pair(yesterday, MaterialDatePicker.todayInUtcMilliseconds())
            ).build()

        dateRangePicker.addOnPositiveButtonClickListener {
            parseSelectedDate(it)
        }
        dateRangePicker.selection?.let { parseSelectedDate(it) }

        binding.chipSelectedDateRange.setOnClickListener {
            if (!dateRangePicker.isAdded) {
                dateRangePicker.show(
                    parentFragmentManager,
                    "datePicker_courier_send_report"
                )
            }
        }
    }

    private fun setDateSelection() {
        datePicker = getDatePicker(isTodaySelected = true, fromNow = true, futureDate = 15)
        datePicker.addOnPositiveButtonClickListener {
            selectedDate = getStringDateFromTimeInMillis(it, "yyyy-MM-dd")
            binding.tilDateSelection.editText?.setText(
                getStringDateFromTimeInMillis(
                    it,
                    "dd MMM yyyy"
                )
            )
        }
        binding.tilDateSelection.editText?.setOnClickListener {
            if (!datePicker.isAdded) {
                datePicker.show(parentFragmentManager, "datePicker_on_hold")
            }
        }
    }

    private fun parseSelectedDate(it: Pair<Long, Long>) {
        startDate = getStringDateFromTimeInMillis(it.first, "yyyy-MM-dd")
        endDate = getStringDateFromTimeInMillis(it.second, "yyyy-MM-dd")

        val dateText = "${
            getStringDateFromTimeInMillis(
                it.first,
                "dd MMM"
            )
        } to ${getStringDateFromTimeInMillis(it.second, "dd MMM")}"
        binding.chipSelectedDateRange.text = dateText
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