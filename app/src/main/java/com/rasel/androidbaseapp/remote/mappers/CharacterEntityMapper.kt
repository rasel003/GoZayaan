package com.rasel.androidbaseapp.remote.mappers

import com.rasel.androidbaseapp.data.models.CharacterEntity
import com.rasel.androidbaseapp.remote.models.CharacterModel
import javax.inject.Inject

class CharacterEntityMapper @Inject constructor(
    private val characterLocationEntityMapper: CharacterLocationEntityMapper
) : EntityMapper<CharacterModel, CharacterEntity> {
    override fun mapFromModel(model: CharacterModel): CharacterEntity {
        return CharacterEntity(
            created = model.created,
            gender = model.gender,
            id = model.id,
            image = model.image,
            characterLocation = characterLocationEntityMapper.mapFromModel(model.characterLocation),
            name = model.name,
            species = model.species,
            status = model.status,
            type = model.type,
            url = model.url,
            isBookMarked = model.isBookMarked
        )
    }
}
