package com.rasel.androidbaseapp.data

import android.content.Context
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.cache.preferences.PreferenceProvider
import com.rasel.androidbaseapp.data.models.Localization
import com.rasel.androidbaseapp.data.models.LocalizationBundle
import com.rasel.androidbaseapp.remote.api.MyApi
import com.rasel.androidbaseapp.util.AppLanguage
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class LocalizationRepository @Inject constructor(
    private val preferenceStorage: PreferenceProvider,
    @ApplicationContext private val context: Context,
    private val myApi: MyApi,
) {

    private val moshiAdapter: JsonAdapter<LocalizationBundle> by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .adapter(LocalizationBundle::class.java)
    }

    fun getLocalizationBundle(): LocalizationBundle {
        // Fetch Localization Bundle from Raw Asset
        val localizationJson = getLocalizationJsonFromLocal()
        val localizationBundle = moshiAdapter.fromJson(localizationJson)
        return localizationBundle ?: LocalizationBundle()
    }

    private fun getLocalizationJsonFromLocal(): String = context.resources
        .openRawResource(R.raw.localization)
        .bufferedReader()
        .use { it.readText() }


    private val _localizationFlow: MutableStateFlow<Localization> by lazy {
        MutableStateFlow(
            getLocalizationBundle().getLocalization(currentAppLanguage)
        )
    }

    val localizationFlow: StateFlow<Localization> = _localizationFlow
    private var cachedLocalizationBundle: LocalizationBundle? = null

    val currentAppLanguage: AppLanguage
        get() = preferenceStorage.getAppLanguage()

    /* init {
         CoroutineScope(Dispatchers.IO).launch {
             getLocalizationFromRemote()
         }
     }*/

    suspend fun updateLanguage(language: AppLanguage) {
        _localizationFlow.value = getLocalization(language)
        preferenceStorage.saveAppLanguage(language)
    }

    suspend fun getLocalizationFromRemote() {
        // Fetch localization data from remote here

        // A Dummy Implementation of fetching Localization from Remote
        delay(5000)
        val localizationBundle = LocalizationBundle(
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

        localizationBundle.let {
            cachedLocalizationBundle = it
            _localizationFlow.value = it.getLocalization(currentAppLanguage)
        }
    }

    private fun getLocalization(language: AppLanguage): Localization =
        cachedLocalizationBundle?.getLocalization(language)
            ?: getLocalizationBundle().getLocalization(language)


    fun LocalizationBundle.getLocalization(language: AppLanguage): Localization = when (language) {
        AppLanguage.ENGLISH -> en
        AppLanguage.CHINESE -> cn
        AppLanguage.BURMESE -> mm
    }
}