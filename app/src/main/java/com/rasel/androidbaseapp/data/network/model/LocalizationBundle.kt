package com.rasel.androidbaseapp.data.network.model

import com.rasel.androidbaseapp.data.network.model.Localization

data class LocalizationBundle(
    val en : Localization = Localization(),
    val cn : Localization = Localization(),
    val mm : Localization = Localization()
)