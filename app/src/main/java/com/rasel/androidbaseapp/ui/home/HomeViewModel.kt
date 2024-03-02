package com.rasel.androidbaseapp.ui.home

import androidx.lifecycle.LiveData
import com.rasel.androidbaseapp.domain.interactor.PhotoListUseCase
import com.rasel.androidbaseapp.presentation.utils.CoroutineContextProvider
import com.rasel.androidbaseapp.presentation.utils.ExceptionHandler
import com.rasel.androidbaseapp.presentation.utils.UiAwareLiveData
import com.rasel.androidbaseapp.presentation.utils.UiAwareModel
import com.rasel.androidbaseapp.presentation.viewmodel.BaseViewModel
import com.rasel.androidbaseapp.remote.models.UnsplashSearchResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject


sealed class PhotoListUIModel : UiAwareModel() {
    object Loading : PhotoListUIModel()
    data class Error(var error: String = "") : PhotoListUIModel()
    data class Success(val data: UnsplashSearchResponse) : PhotoListUIModel()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val photoListUseCase: PhotoListUseCase
) : BaseViewModel(contextProvider) {

    /*private val _unsplashPhoto = MutableLiveData<List<UnsplashPhoto>>()
    val unsplashPhoto : LiveData<List<UnsplashPhoto>> = _unsplashPhoto */

    private val _unsplashPhoto = UiAwareLiveData<PhotoListUIModel>()
    val unsplashPhoto: LiveData<PhotoListUIModel> = _unsplashPhoto

    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        val message = ExceptionHandler.parse(exception)
//        _character.postValue(PhotoListUIModel.Error(exception.message ?: "Error"))
    }

    fun getDataFromUnSplash(query: String = "Fruit") {
        launchCoroutineIO {
            photoListUseCase(query).collect {
                _unsplashPhoto.postValue(PhotoListUIModel.Success(it))
            }
        }
    }
    /* fun getDataFromUnSplash(query: String = "Fruit") {

         viewModelScope.launch {
             try {
                 val response = repository.getDataFromUnSplash(query)
                 if (response.value.body.isNotEmpty()) {
                     _unsplashPhoto.value = response.results

                 } else {
                     Logger.d("Current user available Response is not successful")
                 }
             } catch (e: NoInternetException) {
                 Logger.d(e.message!!)
             } catch (e: ApiException) {
                 Logger.d(e.message!!)
             } catch (e: Exception) {
                 Logger.d(e.message ?: "Exception in bottom catch of current user available leave")
             }
         }
     }*/
}