package com.rasel.gozayaan.remote.models

import com.google.gson.annotations.SerializedName

data class CharacterResponseModel(
    @SerializedName( "results")
    val characters: List<CharacterModel>
)
