package com.rasel.androidbaseapp.ui.settings

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
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
import com.rasel.androidbaseapp.util.FullScreenBottomSheetDialog
import com.rasel.androidbaseapp.util.TimeUtils
import com.rasel.androidbaseapp.util.TimeUtils.getStringDateFromTimeInMillis
import com.rasel.androidbaseapp.util.observe
import com.rasel.androidbaseapp.util.permissionGranted
import com.rasel.androidbaseapp.util.result.EventObserver
import com.rasel.androidbaseapp.util.showPermissionRequestDialog
import com.rasel.androidbaseapp.util.toastInfo
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment2 : BaseFragment<FragmentSettingsBinding, BaseViewModel>(),
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
    private lateinit var downLoadUrl: String
    private var isPdfFileDownloading: Boolean = false
    private lateinit var downloadFile: File
    var downloadID: Long = 0

    private var downloadedFilePath: String? = null

    private var requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                if (::downLoadUrl.isInitialized) {
                    downloadFile(downLoadUrl)
                }
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
            FullScreenBottomSheetDialog.display(
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
            downLoadUrl = "https://filesamples.com/samples/document/xlsx/sample1.xlsx"
//            checkPermissionAndDownload()
            downloadFile()
        }
        binding.chipBottomSheet.setOnClickListener {
            val dialog = DialogInsurancePolicy() {
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
            ThemeSettingDialogFragment.newInstance()
                .show(parentFragmentManager, null)
        })
    }

   /* override fun onDestroyView() {
        super.onDestroyView()
        unregisterDownloadReceiver()
    }*/


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
                    viewModel.onThemeSettingClicked()
                }
                else -> {
                    viewModel.setSettings(selectedSetting)
                }
            }
            Timber.tag("rsl").d(selectedSetting.settingLabel)
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

    private fun registerDownloadReceiver() {
        activity?.registerReceiver(
            onDownloadComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    private fun unregisterDownloadReceiver() {
        activity?.unregisterReceiver(onDownloadComplete)
    }
    private fun checkPermissionAndDownload() {
        var customPermission: String = Manifest.permission.READ_EXTERNAL_STORAGE
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            customPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        }

        when {
            requireContext().permissionGranted(customPermission) -> {
                downloadFile(downLoadUrl)
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
//                requestPermissionLauncher.launch(customPermission)
                downloadFile(downLoadUrl)
            }
        }
    }

    private fun downloadFile(url: String) {
        val fileUri = Uri.parse(url)
        val downloadManager =
            mContext.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(fileUri)
        val filename = if (isPdfFileDownloading) {
            "${Calendar.getInstance().timeInMillis}.xlsx"
        } else {
            "Merchant-Order--${Calendar.getInstance().timeInMillis}.xlsx"
        }

        val folder = getString(R.string.app_name)
        downloadFile = File(FileUtils.commonDownloadDirPath().toString() + File.separator, filename)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setDescription("Downloading...") //Download Manager description
            .setDestinationUri(Uri.fromFile(downloadFile))

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        downloadID = downloadManager.enqueue(request)
        mContext.toastInfo("Downloading...")
    }

    private val onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //Fetching the download id received with the broadcast
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                downLoadCompleted()
            }
        }
    }

    private fun downLoadCompleted() {
        val snackbar = Snackbar.make(binding.root, "Download completed", Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("Open") {
            snackbar.dismiss()
            showDownload()
        }
        snackbar.show()
    }
    private fun showDownload(){
        Timber.tag(TAG).i("Download ID: %s", downloadID)

        // Get file URI
        val dm = mContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query().apply {
            setFilterById(downloadID)
        }
        dm.query(query)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val localUriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                if (statusIndex != -1 && localUriIndex != -1) {
                    val status = cursor.getInt(statusIndex)
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        Timber.tag(TAG).i("Download Complete")
                        Toast.makeText(mContext, "Download Complete", Toast.LENGTH_SHORT).show()

                        val uriString = cursor.getString(localUriIndex)
                        Timber.tag(TAG).i("URI: $uriString")

                        // Convert file:// URI to content:// URI using FileProvider
                        val fileUri = Uri.parse(uriString)
                        val contentUri = FileProvider.getUriForFile(
                            mContext,
                            "${mContext.applicationContext.packageName}.fileprovider",
                            File(fileUri.path!!)
                        )
                        val intentView = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(contentUri, "application/vnd.ms-excel")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        startActivity(intentView)
                    } else {
                        Timber.tag(TAG).w("Download Unsuccessful, Status Code: $status")
                        Toast.makeText(mContext, "Download Unsuccessful", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(mContext, "Column index not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(mContext, "No data found for download ID: $downloadID", Toast.LENGTH_SHORT).show()
            }
        }


    }

   /* private fun showDownload() {
        val intent: Intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val uri = FileProvider.getUriForFile(
                mContext,
                "${mContext.packageName}.fileprovider",
                downloadFile
            )
            intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        } else {
            intent = Intent(Intent.ACTION_VIEW)
            if (isPdfFileDownloading) {
                intent.setDataAndType(Uri.fromFile(downloadFile), "application/pdf")
            } else {
                intent.setDataAndType(
                    Uri.fromFile(downloadFile),
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val title = "Open File"
        // Create intent to show chooser
        val chooser = Intent.createChooser(intent, title)


        // Try to invoke the intent.
        try {
            startActivity(chooser)
        } catch (e: ActivityNotFoundException) {
            // Define what your app should do if no activity can handle the intent.
            val i = Intent()
            //try more options to show downloading , retrieving and complete
            i.action = DownloadManager.ACTION_VIEW_DOWNLOADS
            startActivity(i)
        }
    }*/



    private fun downloadFile() {
        val url = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
        val fileName = "sample.pdf"
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setDescription("Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        val downloadManager =
            requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)

        // Register BroadcastReceiver to listen for download completion
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadID) {
                    // Download completed
                    downloadedFilePath = getDownloadedFilePath(downloadID)
                    val snackbar = Snackbar.make(binding.root, "Download Completed", Snackbar.LENGTH_INDEFINITE)
                    snackbar.setAction("Open") { openDownloadedFile() }
                    snackbar.show()
                    context?.unregisterReceiver(this) // unregister BroadcastReceiver
                }
            }
        }
        requireContext().registerReceiver(
            onComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    private fun getDownloadedFilePath(downloadId: Long): String? {
        val downloadManager =
            requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
            return cursor.getString(columnIndex)
        }
        return null
    }

    private fun openDownloadedFile() {
        downloadedFilePath?.let { path ->
            val file = File(Uri.parse(path).path!!)
            val uri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().applicationContext.packageName + ".fileprovider",
                file
            )
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf") // Change MIME type accordingly
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(intent)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment2()

        private const val TAG = "rsl"
    }


}