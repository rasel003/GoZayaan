package com.rasel.androidbaseapp.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rasel.androidbaseapp.cache.utils.CacheConstants

@Entity(tableName = CacheConstants.CHARACTER_LOCATION_TABLE_NAME)
data class CharacterLocationCacheEntity(
    @PrimaryKey
    @ColumnInfo(name = "location")
    val name: String,
    @ColumnInfo(name = "location_url")
    val url: String
)
