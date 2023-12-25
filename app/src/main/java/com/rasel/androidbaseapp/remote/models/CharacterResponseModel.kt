package com.rasel.androidbaseapp.remote.models

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class CharacterResponseModel(
    @SerializedName( "results")
    val characters: List<CharacterModel>
)
