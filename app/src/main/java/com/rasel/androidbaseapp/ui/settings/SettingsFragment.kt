package com.rasel.androidbaseapp.ui.settings

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.util.Pair
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.base.BaseFragment
import com.rasel.androidbaseapp.core.theme.ThemeUtils
import com.rasel.androidbaseapp.data.models.Localization
import com.rasel.androidbaseapp.databinding.FragmentSettingsBinding
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import com.rasel.androidbaseapp.presentation.viewmodel.LocalizedViewModel
import com.rasel.androidbaseapp.presentation.viewmodel.SettingUIModel
import com.rasel.androidbaseapp.presentation.viewmodel.SettingsViewModel
import com.rasel.androidbaseapp.ui.dialog.BankData
import com.rasel.androidbaseapp.ui.dialog.DialogForBank
import com.rasel.androidbaseapp.util.DialogInsurancePolicy
import com.rasel.androidbaseapp.util.FileUtils
import com.rasel.androidbaseapp.util.OrderUpdateHistoryMerchantDialog
import com.rasel.androidbaseapp.util.TimeUtils
import com.rasel.androidbaseapp.util.TimeUtils.getStringDateFromTimeInMillis
import com.rasel.androidbaseapp.util.observe
import com.rasel.androidbaseapp.util.permissionGranted
import com.rasel.androidbaseapp.util.result.EventObserver
import com.rasel.androidbaseapp.util.showPermissionRequestDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, BaseViewModel>(),
    Toolbar.OnMenuItemClickListener {

    override val viewModel: SettingsViewModel by viewModels()
    private val localizedViewModel: LocalizedViewModel by activityViewModels()

    @Inject
    lateinit var settingsAdapter: SettingsAdapter

    @Inject
    lateinit var themeUtils: ThemeUtils

    private lateinit var dialogForBank: DialogForBank

    private lateinit var dateRangePicker: MaterialDatePicker<Pair<Long, Long>>
    private var endDate: String = ""
    private var startDate: String = ""

    private lateinit var datePicker: MaterialDatePicker<Long>
    private var selectedDate: String = ""

    private lateinit var mContext: Context


    private var requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                /* if (::downLoadUrl.isInitialized) {
                     downloadFile(downLoadUrl)
                 }*/
                downloadFile()
            }
        }

    override fun getViewBinding(): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.toolbar.inflateMenu(R.menu.main)
        binding.toolbar.setOnMenuItemClickListener(this)

//        registerDownloadReceiver()

        val sentences = listOf(
            "The quick brown fox jumps over the lazy dog.",
            "Lorem ipsum dolor sit amet, consecrate disciplining elite.",
            "This is a random sentence.",
            "Kotlin is a modern programming language.",
            "Artificial Intelligence is shaping the future."
        )

        val bankDataList = mutableListOf<BankData>()

        for (i in 1..40) {
            bankDataList.add(BankData(i, sentences.random()))
        }


        dialogForBank = DialogForBank.display(bankDataList, ::addItem)


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
            findNavController().navigate(R.id.owl_graph)
        }
        binding.chipCounter.setOnClickListener {
            findNavController().navigate(R.id.action_global_counter_fragment)
        }

        binding.chipFullScreenBottomSheet.setOnClickListener {
            OrderUpdateHistoryMerchantDialog.display(
                childFragmentManager, "history",
                "it1"
            )
        }
        binding.chipFullscreenDialog.setOnClickListener {
            dialogForBank.show(
                childFragmentManager, "history",
            )
        }
        binding.btnDownload.setOnClickListener {
            checkPermissionAndDownload()
        }
        binding.chipBottomSheet.setOnClickListener {
            val dialog = DialogInsurancePolicy {
                Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
            }
            val args = Bundle()
            args.putBoolean("is_update", false)
            dialog.arguments = args
            dialog.show(parentFragmentManager, "insurance_dialog")
        }
        setDateRangeSelection()
        setDateSelection()

        val adapterOnHold = ArrayAdapter(
            requireContext(),
            R.layout.list_item,
            resources.getStringArray(R.array.hold_reasons_v2)
        )
        adapterOnHold.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        (binding.tilReasonList.editText as? AutoCompleteTextView)?.setAdapter(adapterOnHold)

        observe(viewModel.settings, ::onViewStateChange)
        setupRecyclerView()
        viewModel.getSettings()

        viewModel.navigateToThemeSelector.observe(viewLifecycleOwner, EventObserver {
            ThemeSettingDialogFragment.newInstance()
                .show(parentFragmentManager, null)
        })
        viewModel.navigateToLanguageSelector.observe(viewLifecycleOwner, EventObserver {
            LanguageSettingDialogFragment.newInstance()
                .show(parentFragmentManager, null)
        })
    }

    // when activity has toolbar
    /*override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_logOut -> {
                logOutFromApp(); true
            }

            R.id.action_settings -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }*/

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_logOut -> {
                logOutFromApp(); true
            }

            R.id.action_settings -> {
                true
            }

            else -> false
        }
    }


    private fun logOutFromApp() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.titleLogOut))
            .setMessage(resources.getString(R.string.messageLogOut))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to negative button press
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                // Respond to positive button press
                dialog.dismiss()
                activity?.finish()
            }
            .show()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewSettings.apply {
            adapter = settingsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        settingsAdapter.setItemClickListener { selectedSetting ->
            when (selectedSetting.id) {
                3 -> {
                    viewModel.onThemeSettingClicked()
                }

                4 -> {
                    viewModel.onLanguageSettingClicked()
                }

                else -> {
                    viewModel.setSettings(selectedSetting)
                }
            }
            Timber.d(selectedSetting.settingLabel)
        }
    }

    private fun setDateRangeSelection() {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val yesterday = calendar.timeInMillis
        dateRangePicker = TimeUtils.getDateRangePicker()
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
        datePicker = TimeUtils.getDatePicker(isTodaySelected = true, fromNow = true, futureDate = 15)
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

    private fun addItem(bankData: BankData?) {
        Toast.makeText(requireContext(), bankData?.bankTitle, Toast.LENGTH_SHORT).show()
        dialogForBank.dismiss()
    }
   // mnopqrs

    private fun checkPermissionAndDownload() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            downloadFile()
        }else {
            var customPermission: String = Manifest.permission.READ_EXTERNAL_STORAGE
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                customPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
            }
            when {
                requireContext().permissionGranted(customPermission) -> {
                    downloadFile()
                }

                shouldShowRequestPermissionRationale(customPermission) -> {
                    requireContext().showPermissionRequestDialog(
                        getString(R.string.permission_title),
                        getString(R.string.write_permission_request)
                    ) {
                        requestPermissionLauncher.launch(customPermission)
                    }
                }

                else -> {
                    requestPermissionLauncher.launch(customPermission)
                }
            }
        }

    }

    private fun downloadFile() {
//        val downLoadUrl = "https://filesamples.com/samples/document/xlsx/sample1.xlsx"
        val downLoadUrl = "https://sample-videos.com/xls/Sample-Spreadsheet-50000-rows.xls"

//        val downLoadUrl = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
//        val downLoadUrl = "https://file-examples.com/wp-content/storage/2017/02/file_example_XLSX_5000.xlsx"

        FileUtils.downloadFile(
            url = downLoadUrl,
            context = mContext,
//            fileName = fileName
        ) { downloadId: Long, extension: String ->
            onDownLoadCompleted(downloadId, extension)
        }
    }

    private fun onDownLoadCompleted(downloadID: Long, extension: String) {
        val uri = FileUtils.getDownloadedFilePath(downloadID, mContext)

        val snackbar = Snackbar.make(binding.root, "Download completed", Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("Open") {
            snackbar.dismiss()
            uri?.let { it1 -> FileUtils.openDownloadedFile(it1, it.context, extension) }
        }
        snackbar.show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()

        private const val TAG = "rsl"
    }


}