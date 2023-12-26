package com.rasel.androidbaseapp.fakes

import com.rasel.androidbaseapp.data.models.CharacterEntity
import com.rasel.androidbaseapp.data.models.CharacterLocationEntity
import com.rasel.androidbaseapp.fakes.FakeValueFactory.randomInt
import com.rasel.androidbaseapp.fakes.FakeValueFactory.randomString
import com.rasel.androidbaseapp.remote.models.CharacterLocationModel
import com.rasel.androidbaseapp.remote.models.CharacterModel
import com.rasel.androidbaseapp.remote.models.CharacterResponseModel

object FakeRemoteData {

    fun getResponse(size: Int, isRandomId: Boolean = true): CharacterResponseModel {
        return CharacterResponseModel(getFakeCharacterModel(size, isRandomId))
    }

    private fun getFakeCharacterModel(size: Int, isRandomId: Boolean): List<CharacterModel> {
        val characters = mutableListOf<CharacterModel>()
        repeat(size) {
            characters.add(getCharacterModel(isRandomId))
        }
        return characters
    }

    fun getCharacterModel(isRandomId: Boolean): CharacterModel {
        return CharacterModel(
            created = randomString(),
            gender = randomString(),
            id = if (isRandomId) randomInt() else 1,
            image = randomString(),
            characterLocation = CharacterLocationModel(
                name = randomString(),
                url = randomString()
            ),
            name = randomString(),
            species = randomString(),
            status = randomString(),
            type = randomString(),
            url = randomString(),
            isBookMarked = false
        )
    }

    fun getCharacterEntity(isRandomId: Boolean): CharacterEntity {
        return CharacterEntity(
            created = randomString(),
            gender = randomString(),
            id = if (isRandomId) randomInt() else 1,
            image = randomString(),
            characterLocation = CharacterLocationEntity(
                name = randomString(),
                url = randomString()
            ),
            name = randomString(),
            species = randomString(),
            status = randomString(),
            type = randomString(),
            url = randomString(),
            isBookMarked = false
        )
    }
}
