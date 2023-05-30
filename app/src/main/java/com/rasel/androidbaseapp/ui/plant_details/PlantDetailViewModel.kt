package com.rasel.androidbaseapp.ui.plant_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rasel.androidbaseapp.BuildConfig
import com.rasel.androidbaseapp.data.repositories.PlantRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * The ViewModel used in [PlantDetailFragment].
 */

@HiltViewModel
class PlantDetailViewModel @Inject constructor(
    plantRepository: PlantRepository,
    private val state: SavedStateHandle
) : ViewModel() {


    val plant = state.get<String>("plantId")?.let { plantRepository.getPlant(it).asLiveData() }

    fun hasValidUnsplashKey() = (BuildConfig.UNSPLASH_ACCESS_KEY != "null")

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
