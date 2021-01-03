package com.rasel.androidbaseapp.data.network.responses

import com.rasel.androidbaseapp.data.db.entities.User

data class LoginResponse(
    val isSuccessful : Boolean?,
    val fcm_token: String?,
    val error: String?,
    val user: User?,
    val token: String?,
    val role_id: Int?
)
