package com.rasel.androidbaseapp.ui.plant_details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.rasel.androidbaseapp.BuildConfig
import com.rasel.androidbaseapp.data.repositories.PlantRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

/**
 * The ViewModel used in [PlantDetailFragment].
 */
class PlantDetailViewModel @ViewModelInject constructor(
    plantRepository: PlantRepository,
    @Assisted private val plantId: String
) : ViewModel() {


    val plant = plantRepository.getPlant(plantId).asLiveData()

    fun hasValidUnsplashKey() = (BuildConfig.UNSPLASH_ACCESS_KEY != "null")

    @AssistedInject.Factory
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
    }
}
