package com.rasel.androidbaseapp.fakes

import com.rasel.androidbaseapp.fakes.FakeValueFactory.randomInt
import com.rasel.androidbaseapp.fakes.FakeValueFactory.randomString
import com.rasel.androidbaseapp.domain.models.SettingType
import com.rasel.androidbaseapp.domain.models.Settings
import com.rasel.androidbaseapp.domain.models.Character
import com.rasel.androidbaseapp.domain.models.CharacterLocation
import com.rasel.androidbaseapp.fakes.FakeValueFactory.randomBoolean


object FakePresentationData {

    fun getCharacters(
        size: Int,
        isRandomId: Boolean = true,
        isBookmarked: Boolean = false
    ): List<Character> {
        val characters = mutableListOf<Character>()
        repeat(size) {
            characters.add(createCharacter(isRandomId, isBookmarked))
        }
        return characters
    }

    fun getSettings(size: Int): List<Settings> {
        val settings = mutableListOf<Settings>()
        repeat(size) {
            settings.add(createSetting())
        }
        return settings
    }

    private fun createCharacter(isRandomId: Boolean, isBookmarked: Boolean): Character {
        return Character(
            created = randomString(),
            gender = randomString(),
            id = if (isRandomId) randomInt() else 1,
            image = randomString(),
            characterLocation = CharacterLocation(
                name = randomString(),
                url = randomString()
            ),
            name = randomString(),
            species = randomString(),
            status = randomString(),
            randomString(),
            url = randomString(),
            isBookMarked = if (isBookmarked) true else randomBoolean()
        )
    }

    private fun createSetting(): Settings {
        return Settings(
            id = randomInt(),
            type = SettingType.SWITCH,
            settingLabel = randomString(),
            settingValue = randomString()
        )
    }
}
