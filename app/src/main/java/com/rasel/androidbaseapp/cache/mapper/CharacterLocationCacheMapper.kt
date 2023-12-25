package com.rasel.androidbaseapp.cache.mapper

import com.rasel.androidbaseapp.cache.entities.CharacterLocationCacheEntity
import com.rasel.androidbaseapp.data2.models.CharacterLocationEntity
import javax.inject.Inject

class CharacterLocationCacheMapper @Inject constructor() :
    CacheMapper<CharacterLocationCacheEntity, CharacterLocationEntity> {
    override fun mapFromCached(type: CharacterLocationCacheEntity): CharacterLocationEntity {
        return CharacterLocationEntity(name = type.name, url = type.url)
    }

    override fun mapToCached(type: CharacterLocationEntity): CharacterLocationCacheEntity {
        return CharacterLocationCacheEntity(name = type.name, url = type.url)
    }
}
