package com.rasel.androidbaseapp.data.repositories

import com.rasel.androidbaseapp.data.db.AppDatabase
import com.rasel.androidbaseapp.data.network.MyApi
import com.rasel.androidbaseapp.data.network.SafeApiRequest
import com.rasel.androidbaseapp.data.preferences.PreferenceProvider

class MainRepository(
    private val api: MyApi,
    private val db: AppDatabase,
    private val prefs: PreferenceProvider
) : SafeApiRequest() {

    fun getUser() = db.getUserDao().getuser()

}