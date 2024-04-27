package com.rasel.androidbaseapp.data

import android.util.Log
import com.rasel.androidbaseapp.data.mapper.CharacterMapper
import com.rasel.androidbaseapp.data.source.CharacterDataSourceFactory
import com.rasel.androidbaseapp.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.rasel.androidbaseapp.domain.models.Character
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject

class CharacterRepositoryImp @Inject constructor(
    private val dataSourceFactory: CharacterDataSourceFactory,
    private val characterMapper: CharacterMapper,
) : CharacterRepository {

    private val TAG = "CharacterRepositoryImp"
    override suspend fun getCharacters(): Flow<List<Character>> = flow {

        Timber.tag(TAG).d("getCharacters: get called")
//        delay(6000)
        val isCached = dataSourceFactory.getCacheDataSource().isCached()
        val characterList =
            dataSourceFactory.getDataStore(isCached).getCharacters().map { characterEntity ->
                characterMapper.mapFromEntity(characterEntity)
            }
        saveCharacters(characterList)
        emit(characterList)
    }

    override suspend fun getCharacter(characterId: Long): Flow<Character> = flow {
        var character = dataSourceFactory.getCacheDataSource().getCharacter(characterId)
        if (character.image.isEmpty()) {
            character = dataSourceFactory.getRemoteDataSource().getCharacter(characterId)
        }
        emit(
            characterMapper.mapFromEntity(character)
        )
    }

    override suspend fun saveCharacters(listCharacters: List<Character>) {
        val characterEntities = listCharacters.map { character ->
            characterMapper.mapToEntity(character)
        }
        dataSourceFactory.getCacheDataSource().saveCharacters(characterEntities)
    }

    override suspend fun getBookMarkedCharacters(): Flow<List<Character>> = flow {
        val characterList = dataSourceFactory.getCacheDataSource().getBookMarkedCharacters()
            .map { characterEntity ->
                characterMapper.mapFromEntity(characterEntity)
            }
        emit(characterList)
    }

    override suspend fun setCharacterBookmarked(characterId: Long): Flow<Int> = flow {
        emit(dataSourceFactory.getCacheDataSource().setCharacterBookmarked(characterId))
    }

    override suspend fun setCharacterUnBookMarked(characterId: Long): Flow<Int> = flow {
        emit(dataSourceFactory.getCacheDataSource().setCharacterUnBookMarked(characterId))
    }
}
