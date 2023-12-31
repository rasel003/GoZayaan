package com.rasel.androidbaseapp.fakes

import com.rasel.androidbaseapp.data.models.Localization
import com.rasel.androidbaseapp.data.models.LocalizationBundle
import com.rasel.androidbaseapp.data.models.getLocalization
import com.rasel.androidbaseapp.domain.models.Character
import com.rasel.androidbaseapp.domain.models.CharacterLocation
import com.rasel.androidbaseapp.domain.models.SettingType
import com.rasel.androidbaseapp.domain.models.Settings
import com.rasel.androidbaseapp.util.AppLanguage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object FakeData {
    fun getCharacters(): Flow<List<Character>> = flow {
        val characters = listOf(
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
            ),
            Character(
                "01/02/2021",
                "Male",
                2,
                "https://dummyurl.png",
                CharacterLocation("Earth", "https://dummy.url"),
                "Morty",
                "Human",
                "Alive",
                "",
                "",
                false
            )
        )
        emit(characters)
    }

    fun getCharacter(): Flow<Character> = flow {
        emit(
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
        )
    }

    fun getSettings(isNightMode: Boolean): Flow<List<Settings>> = flow {
        val settings = listOf(
            Settings(1, SettingType.SWITCH, "Theme mode", "", isNightMode),
            Settings(2, SettingType.EMPTY, "Clear cache", ""),
            Settings(2, SettingType.TEXT, "App version", "1.0")
        )
        emit(settings)
    }

    fun getLocalization(language: AppLanguage): Localization {
        // A Dummy Implementation of fetching Localization from Remote
        val bundle = LocalizationBundle(
            en = Localization().apply {
                lblGreeting = "Hello from Remote"
                lblSelectedLanguage = "Selected Language From Remote : %@"
                lblEnglish = "English"
                lblChinese = "Chinese"
                lblBurmese = "Burmese"
            },
            mm = Localization().apply {
                lblGreeting = "မင်္ဂလာပါ။ from Remote"
                lblSelectedLanguage = "ရွေးချယ်ထားသောဘာသာစကား From Remote : %@"
                lblEnglish = "အင်္ဂလိပ်"
                lblChinese = "တရုတ်"
                lblBurmese = "ဗမာ"
            },
            cn = Localization().apply {
                lblGreeting = "你好 from Remote"
                lblSelectedLanguage = "选择的语言 From Remote : %@"
                lblEnglish = "英语"
                lblChinese = "缅甸语"
                lblBurmese = "中文"
            }
        )
        return bundle.getLocalization(language)
    }
}
