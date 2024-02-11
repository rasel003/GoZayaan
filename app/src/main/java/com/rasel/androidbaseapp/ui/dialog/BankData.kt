package com.rasel.androidbaseapp.ui.dialog

import com.google.gson.annotations.SerializedName

class BankData(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("bank_title") val bankTitle: String
)
