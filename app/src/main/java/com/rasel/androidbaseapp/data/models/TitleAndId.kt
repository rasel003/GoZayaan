package com.rasel.androidbaseapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class TitleAndId (
    val title : String,
    val id: Int
) : Parcelable