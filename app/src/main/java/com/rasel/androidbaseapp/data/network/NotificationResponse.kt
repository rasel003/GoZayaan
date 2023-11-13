package com.rasel.androidbaseapp.data.network

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    val draw: Int,
    val page: Int,
    val limit: Int,
    val totalFiltered: Int,
    val totalRecords: Int,
    val records: List<Records>
)

data class Records(
    val id: String?,
    @SerializedName("title")
    val subject: String?,
    val body: String?,
    val date_sent: String?,
    val date_read: String?
)