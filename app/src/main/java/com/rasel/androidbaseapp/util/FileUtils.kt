package com.rasel.androidbaseapp.util

import android.content.Intent
import android.os.Build
import android.os.Environment
import android.webkit.MimeTypeMap
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

    fun commonDocumentDirPath(FolderName: String): File {
        val dir: File = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    .toString() + "/" + FolderName
            )
        } else {
            File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + Environment.DIRECTORY_DOCUMENTS + "/" + FolderName
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
}