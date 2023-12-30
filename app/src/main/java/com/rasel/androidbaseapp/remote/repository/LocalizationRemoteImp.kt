package com.rasel.androidbaseapp.remote.repository

import com.rasel.androidbaseapp.data.models.Localization
import com.rasel.androidbaseapp.data.models.LocalizationBundle
import com.rasel.androidbaseapp.data.models.getLocalization
import com.rasel.androidbaseapp.data.repository.LocalizationRemote
import com.rasel.androidbaseapp.util.AppLanguage
import kotlinx.coroutines.delay
import javax.inject.Inject

class LocalizationRemoteImp @Inject constructor(): LocalizationRemote {

    override suspend fun getLocalization(language: AppLanguage): Localization {
        delay(2000)

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