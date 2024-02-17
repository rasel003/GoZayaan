package com.rasel.androidbaseapp.util

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Environment
import android.webkit.MimeTypeMap
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import timber.log.Timber
import java.io.File
import java.util.*

object FileUtils {
    private const val TAG = "FileUtils"
    const val ZIP = "application/zip"
    const val DOC = "application/msword"
    const val DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    const val TEXT = "text/*"
    const val PDF = "application/pdf"
    const val XLS = "application/vnd.ms-excel"
    const val XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    const val PPT = "application/vnd.ms-powerpoint"
    const val PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation"
    const val IMAGE = "image/*"
    const val AUDIO = "audio/*"

    fun commonDocumentDirPath(folderName: String): File {
        val dir: File = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    .toString() + "/" + folderName
            )
        } else {
            File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + Environment.DIRECTORY_DOCUMENTS + "/" + folderName
            )
        }
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir
    }

    fun commonDownloadDirPath(): File {
        val dir: File = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + ""
            )
        } else {
            File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + Environment.DIRECTORY_DOWNLOADS
            )
        }
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir
    }

    fun getCustomFileChooserIntent(vararg types: String): Intent {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        // Filter to only show results that can be "opened"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, types)
        return intent
    }

    fun getMimeType(file: File): String {
        var type: String? = null
        val url = file.toString()
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(extension.lowercase(Locale.getDefault()))
        }
        if (type == null) {
            type = "application/*" // fallback type. You might set it to */*
        }
        return type
    }


    private fun getMimeType(fileExtension: String): String {
        // Add MIME types for various file extensions
        return when (fileExtension.lowercase(Locale.ROOT)) {
            "pdf" -> "application/pdf"
            "doc", "docx" -> "application/msword"
            "xls", "xlsx" -> "application/vnd.ms-excel"
            "ppt", "pptx" -> "application/vnd.ms-powerpoint"
            else -> "application/octet-stream" // Default MIME type for other types of files
        }
    }

    fun openDownloadedFile(uri: Uri, context: Context, extension: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, getMimeType(extension))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(intent)
    }

    fun getDownloadedFilePath(downloadId: Long, context: Context): Uri? {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            /*val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
            return cursor.getString(columnIndex)*/

            val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            val localUriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
            if (statusIndex != -1 && localUriIndex != -1) {
                val status = cursor.getInt(statusIndex)
                if (status == DownloadManager.STATUS_SUCCESSFUL) {

                    val uriString = cursor.getString(localUriIndex)

                    // Convert file:// URI to content:// URI using FileProvider
                    val fileUri = Uri.parse(uriString)
                    return FileProvider.getUriForFile(
                        context,
                        "${context.applicationContext.packageName}.fileprovider",
                        File(fileUri.path!!)
                    )
                } else {
                    Timber.w("Download Unsuccessful, Status Code: $status")
                    Toast.makeText(context, "Download Unsuccessful", Toast.LENGTH_SHORT).show()
                    return null
                }
            } else {
                return null
            }
        }
        return null
    }

    private fun getFileNameFromUrl(url: String): String {
        // Remove query parameters and fragment identifiers
        val cleanUrl = url.split("?")[0].split("#")[0]

        // Split the URL by "/" to get individual components
        val components = cleanUrl.split("/")

        // The last component should be the file name
        val fileName = components.last()

        // If the file name is empty or ends with a trailing slash, try the second last component
        return if (fileName.isEmpty() || fileName.endsWith("/")) {
            components.getOrNull(components.size - 2) ?: "unknown"
        } else {
            "${Calendar.getInstance().timeInMillis}+$fileName"
//            fileName
        }
    }

    fun downloadFile(
        url: String,
        fileName: String = "",
        context: Context,
        onDownloadComplete: ((downloadId: Long, extension: String) -> Unit)
    ) {

        val uri = Uri.parse(url)
        val fileNameTemp = fileName.ifEmpty { getFileNameFromUrl(url) }
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)

        val request = DownloadManager.Request(uri)
            .setTitle(fileNameTemp)
            .setDescription("Downloading...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileNameTemp)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadID = downloadManager.enqueue(request)

        // Register BroadcastReceiver to listen for download completion
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadID) {
                    // Download completed
                    onDownloadComplete(downloadID, extension)
                    context?.unregisterReceiver(this) // unregister BroadcastReceiver
                }
            }
        }
        context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }
}