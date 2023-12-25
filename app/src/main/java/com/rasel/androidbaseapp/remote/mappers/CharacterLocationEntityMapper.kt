package com.rasel.androidbaseapp.remote.mappers

import com.rasel.androidbaseapp.data2.models.CharacterLocationEntity
import com.rasel.androidbaseapp.remote.models.CharacterLocationModel
import javax.inject.Inject

class CharacterLocationEntityMapper @Inject constructor() :
    EntityMapper<CharacterLocationModel, CharacterLocationEntity> {
    override fun mapFromModel(model: CharacterLocationModel): CharacterLocationEntity {
        return CharacterLocationEntity(name = model.name, url = model.url)
    }
}
