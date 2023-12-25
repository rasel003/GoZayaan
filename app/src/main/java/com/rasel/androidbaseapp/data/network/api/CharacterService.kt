package com.rasel.androidbaseapp.data.network.api

import com.rasel.androidbaseapp.remote.models.CharacterResponseModel
import com.rasel.androidbaseapp.remote.models.CharacterModel
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterService {

    @GET("character")
    suspend fun getCharacters(): CharacterResponseModel

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Long): CharacterModel
}
