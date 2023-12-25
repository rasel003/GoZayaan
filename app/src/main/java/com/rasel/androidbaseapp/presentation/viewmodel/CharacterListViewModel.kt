package com.rasel.androidbaseapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.rasel.androidbaseapp.presentation.utils.CoroutineContextProvider
import com.rasel.androidbaseapp.presentation.utils.ExceptionHandler
import com.rasel.androidbaseapp.domain.interactor.GetBookmarkCharacterListUseCase
import com.rasel.androidbaseapp.domain.interactor.GetCharacterListUseCase
import com.rasel.androidbaseapp.presentation.utils.UiAwareLiveData
import com.rasel.androidbaseapp.presentation.utils.UiAwareModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject
import com.rasel.androidbaseapp.domain.models.Character

private const val TAG = "CharacterListViewModel"

sealed class CharacterUIModel : UiAwareModel() {
    object Loading : CharacterUIModel()
    data class Error(var error: String = "") : CharacterUIModel()
    data class Success(val data: List<Character>) : CharacterUIModel()
}

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val characterListUseCase: GetCharacterListUseCase,
    private val bookmarkCharacterListUseCase: GetBookmarkCharacterListUseCase
) : BaseViewModel(contextProvider) {

    private val _characterList = UiAwareLiveData<CharacterUIModel>()
    private var characterList: LiveData<CharacterUIModel> = _characterList

    fun getCharacters(): LiveData<CharacterUIModel> {
        return characterList
    }

    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        val message = ExceptionHandler.parse(exception)
        _characterList.postValue(CharacterUIModel.Error(exception.message ?: "Error"))
    }

    fun getCharacters(isFavorite: Boolean) {
        _characterList.postValue(CharacterUIModel.Loading)
        launchCoroutineIO {
            if (isFavorite)
                loadFavoriteCharacters()
            else
                loadCharacters()
        }
    }

    private suspend fun loadCharacters() {
        characterListUseCase(Unit).collect {
            _characterList.postValue(CharacterUIModel.Success(it))
        }
    }

    private suspend fun loadFavoriteCharacters() {
        bookmarkCharacterListUseCase(Unit).collect {
            _characterList.postValue(CharacterUIModel.Success(it))
        }
    }
}
