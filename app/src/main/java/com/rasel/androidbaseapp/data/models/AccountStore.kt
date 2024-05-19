/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rasel.androidbaseapp.data.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.util.FakeValueFactory

/**
 * An static data store of [Account]s. This includes both [Account]s owned by the current user and
 * all [Account]s of the current user's contacts.
 */
object AccountStore {

    private val allUserAccounts = mutableListOf(
        Account(
            1L,
            0L,
            "Jeff",
            "Hansen",
            "hikingfan@gmail.com",
            "hkngfan@outside.com",
            FakeValueFactory.getFixedImageList().random(),
            true
        ),
        Account(
            2L,
            0L,
            "Jeff",
            "H",
            "jeffersonloveshiking@gmail.com",
            "jeffersonloveshiking@work.com",
            FakeValueFactory.getFixedImageList().random()
        ),
        Account(
            3L,
            0L,
            "Jeff",
            "Hansen",
            "jeffersonc@google.com",
            "jeffersonc@gmail.com",
            FakeValueFactory.getFixedImageList().random()
        )
    )

    private val allUserContactAccounts = listOf(
        Account(
            4L,
            1L,
            "Tracy",
            "Alvarez",
            "tracealvie@gmail.com",
            "tracealvie@gravity.com",
            FakeValueFactory.getFixedImageList().random()
        ),
        Account(
            5L,
            2L,
            "Allison",
            "Trabucco",
            "atrabucco222@gmail.com",
            "atrabucco222@work.com",
            FakeValueFactory.getFixedImageList().random()
        ),
        Account(
            6L,
            3L,
            "Ali",
            "Connors",
            "aliconnors@gmail.com",
            "aliconnors@android.com",
            FakeValueFactory.getFixedImageList().random()
        ),
        Account(
            7L,
            4L,
            "Alberto",
            "Williams",
            "albertowilliams124@gmail.com",
            "albertowilliams124@chromeos.com",
            FakeValueFactory.getFixedImageList().random()
        ),
        Account(
            8L,
            5L,
            "Kim",
            "Alen",
            "alen13@gmail.com",
            "alen13@mountainview.gov",
            FakeValueFactory.getFixedImageList().random()
        ),
        Account(
            9L,
            6L,
            "Google",
            "Express",
            "express@google.com",
            "express@gmail.com",
            FakeValueFactory.getFixedImageList().random()
        ),
        Account(
            10L,
            7L,
            "Sandra",
            "Adams",
            "sandraadams@gmail.com",
            "sandraadams@textera.com",
            FakeValueFactory.getFixedImageList().random()
        ),
        Account(
            11L,
            8L,
            "Trevor",
            "Hansen",
            "trevorhandsen@gmail.com",
            "trevorhandsen@express.com",
            FakeValueFactory.getFixedImageList().random()
        ),
        Account(
            12L,
            9L,
            "Sean",
            "Holt",
            "sholt@gmail.com",
            "sholt@art.com",
            FakeValueFactory.getFixedImageList().random()
        ),
        Account(
            13L,
            10L,
            "Frank",
            "Hawkins",
            "fhawkank@gmail.com",
            "fhawkank@thisisme.com",
            FakeValueFactory.getFixedImageList().random()
        )
    )

    private val _userAccounts: MutableLiveData<List<Account>> = MutableLiveData()
    val userAccounts: LiveData<List<Account>>
        get() = _userAccounts

    init {
        postUpdateUserAccountsList()
    }

    /**
     * Get the current user's default account.
     */
    fun getDefaultUserAccount() = allUserAccounts.first()

    /**
     * Get all [Account]s owned by the current user.
     */
    fun getAllUserAccounts() = allUserAccounts

    /**
     * Whether or not the given [Account.id] uid is an account owned by the current user.
     */
    fun isUserAccount(uid: Long): Boolean = allUserAccounts.any { it.uid == uid }

    fun setCurrentUserAccount(accountId: Long): Boolean {
        var updated = false
        allUserAccounts.forEachIndexed { index, account ->
            val shouldCheck = account.id == accountId
            if (account.isCurrentAccount != shouldCheck) {
                allUserAccounts[index] = account.copy(isCurrentAccount = shouldCheck)
                updated = true
            }
        }
        if (updated) postUpdateUserAccountsList()
        return updated
    }

    private fun postUpdateUserAccountsList() {
        val newList = allUserAccounts.toList()
        _userAccounts.value = newList
    }

    /**
     * Get the contact of the current user with the given [accountId].
     */
    fun getContactAccountById(accountId: Long): Account {
        return allUserContactAccounts.firstOrNull { it.id == accountId } ?: allUserContactAccounts.first()
    }
}