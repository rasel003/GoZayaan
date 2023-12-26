package com.rasel.androidbaseapp.remote.models


import com.google.gson.annotations.SerializedName

data class PostItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("userId")
    val userId: Int
)