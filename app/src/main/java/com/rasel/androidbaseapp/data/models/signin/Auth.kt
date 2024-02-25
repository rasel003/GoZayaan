package com.rasel.androidbaseapp.data.models.signin

import com.google.gson.annotations.SerializedName

data class Auth(
    @SerializedName("email")
    val email: String,
    val password: String
)