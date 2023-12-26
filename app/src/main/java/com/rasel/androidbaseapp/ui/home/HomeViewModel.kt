package com.rasel.androidbaseapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.rasel.androidbaseapp.data.models.UnsplashPhoto
import com.rasel.androidbaseapp.data.HomeRepository
import com.rasel.androidbaseapp.util.ApiException
import com.rasel.androidbaseapp.util.NoInternetException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    private val _unsplashPhoto = MutableLiveData<List<UnsplashPhoto>>()

    val unsplashPhoto : LiveData<List<UnsplashPhoto>> = _unsplashPhoto

    fun getDataFromUnSplash(query: String = "Fruit") {

        viewModelScope.launch {
            try {
                val response = repository.getDataFromUnSplash(query)
               /* if (response.value.body.isNotEmpty()) {
                    _unsplashPhoto.value = response.results
                   
                } else {
                    Logger.d("Current user available Response is not successful")
                }*/
            } catch (e: NoInternetException) {
                Logger.d(e.message!!)
            } catch (e: ApiException) {
                Logger.d(e.message!!)
            } catch (e: Exception) {
                Logger.d(e.message ?: "Exception in bottom catch of current user available leave")
            }
        }
    }
}