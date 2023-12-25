package com.rasel.androidbaseapp.cache.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Designation(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String
)