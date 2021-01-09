package com.rasel.androidbaseapp.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.rasel.androidbaseapp.data.network.model.UnsplashPhoto
import com.rasel.androidbaseapp.data.repositories.HomeRepository
import com.rasel.androidbaseapp.util.ApiException
import com.rasel.androidbaseapp.util.NoInternetException
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    private val _unsplashPhoto = MutableLiveData<List<UnsplashPhoto>>()

    val unsplashPhoto : LiveData<List<UnsplashPhoto>> = _unsplashPhoto

    fun getDataFromUnSplash(query: String = "Fruit") {

        viewModelScope.launch {
            try {
                val response = repository.getDataFromUnSplash(query)
                if (response.results.isNotEmpty()) {
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
    }
}