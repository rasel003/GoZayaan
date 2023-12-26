package com.rasel.androidbaseapp.ui.plant_details

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bumptech.glide.RequestManager
import com.rasel.androidbaseapp.BuildConfig
import com.rasel.androidbaseapp.data.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * The ViewModel used in [PlantDetailFragment].
 */

@HiltViewModel
class PlantDetailViewModel @Inject constructor(
    plantRepository: HomeRepository,
    private var imageLoader: RequestManager,
    state: SavedStateHandle
) : ViewModel() {


    val plant = state.get<String>("plantId")?.let { plantRepository.getPlant(it).asLiveData() }

    fun hasValidUnsplashKey() = (BuildConfig.UNSPLASH_ACCESS_KEY != "null")

    // Function to load image asynchronously using coroutines
    suspend fun loadImageAsync(mediaDownloadURL: String): Bitmap {
        return withContext(Dispatchers.IO) {
            // Perform image loading on the IO dispatcher (background thread)
            return@withContext imageLoader
                .asBitmap()
                .load(mediaDownloadURL)
                .submit()
                .get()
        }
    }

   /* @AssistedInject.Factory
    interface AssistedFactory {
        fun create(plantId: String): PlantDetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            plantId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(plantId) as T
            }
        }
    }*/
}
