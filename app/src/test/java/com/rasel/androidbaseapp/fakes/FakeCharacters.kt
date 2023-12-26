package com.rasel.androidbaseapp.fakes

import com.rasel.androidbaseapp.data.models.CharacterEntity
import com.rasel.androidbaseapp.data.models.CharacterLocationEntity
import com.rasel.androidbaseapp.domain.models.Character
import com.rasel.androidbaseapp.domain.models.CharacterLocation

object FakeCharacters {
    fun getCharacters(): List<CharacterEntity> = listOf(
        CharacterEntity(
            "01/02/2021",
            "Male",
            1,
            "https://dummyurl.png",
            CharacterLocationEntity("Earth", "https://dummy.url"),
            "Rick",
            "Human",
            "Alive",
            "",
            "",
            false
        ),
        CharacterEntity(
            "01/02/2021",
            "Male",
            2,
            "https://dummyurl.png",
            CharacterLocationEntity("Earth", "https://dummy.url"),
            "Morty",
            "Human",
            "Alive",
            "",
            "",
            false
        )
    )

    fun getCharacter(): Character =
        Character(
            "01/02/2021",
            "Male",
            1,
            "https://dummyurl.png",
            CharacterLocation("Earth", "https://dummy.url"),
            "Rick",
            "Human",
            "Alive",
            "",
            "",
            false
        )
}
