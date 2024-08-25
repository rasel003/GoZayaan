package com.rasel.androidbaseapp.base

import android.Manifest
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import com.rasel.androidbaseapp.util.FileUtils
import com.rasel.androidbaseapp.util.permissionGranted
import com.rasel.androidbaseapp.util.showPermissionRequestDialog

abstract class DownloadFragment<VB : ViewBinding, ViewModel : BaseViewModel> : BaseFragment<VB, ViewModel>() {

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

    fun checkPermissionAndDownload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            downloadFile()
        } else {
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

}
