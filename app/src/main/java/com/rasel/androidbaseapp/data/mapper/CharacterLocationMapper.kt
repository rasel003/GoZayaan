package com.rasel.androidbaseapp.data.mapper

import com.rasel.androidbaseapp.data.models.CharacterLocationEntity
import com.rasel.androidbaseapp.domain.models.CharacterLocation
import javax.inject.Inject

class CharacterLocationMapper @Inject constructor() :
    Mapper<CharacterLocationEntity, CharacterLocation> {

    override fun mapFromEntity(type: CharacterLocationEntity): CharacterLocation {
        return CharacterLocation(name = type.name, url = type.url)
    }

    override fun mapToEntity(type: CharacterLocation): CharacterLocationEntity {
        return CharacterLocationEntity(name = type.name, url = type.url)
    }
}
