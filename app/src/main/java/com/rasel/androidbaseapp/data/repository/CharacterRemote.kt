package com.rasel.androidbaseapp.data.repository

import com.rasel.androidbaseapp.data.models.CharacterEntity

interface CharacterRemote {
    suspend fun getCharacters(): List<CharacterEntity>
    suspend fun getCharacter(characterId: Long): CharacterEntity
}
