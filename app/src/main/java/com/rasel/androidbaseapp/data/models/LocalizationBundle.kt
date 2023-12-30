package com.rasel.androidbaseapp.data.models

import com.rasel.androidbaseapp.util.AppLanguage

data class LocalizationBundle(
    val en : Localization = Localization(),
    val cn : Localization = Localization(),
    val mm : Localization = Localization()
)
fun LocalizationBundle.getLocalization(language: AppLanguage): Localization
        = when(language) {
    AppLanguage.ENGLISH -> en
    AppLanguage.CHINESE -> cn
    AppLanguage.BURMESE -> mm
}