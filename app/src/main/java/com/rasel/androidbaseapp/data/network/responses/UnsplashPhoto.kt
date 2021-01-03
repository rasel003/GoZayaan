
package com.rasel.androidbaseapp.data.network.responses

import com.google.gson.annotations.SerializedName
import com.google.samples.apps.sunflower.data.UnsplashPhotoUrls
import com.rasel.androidbaseapp.data.network.model.UnsplashUser

/**
 * Data class that represents a photo from Unsplash.
 *
 * Not all of the fields returned from the API are represented here; only the ones used in this
 * project are listed below. For a full list of fields, consult the API documentation
 * [here](https://unsplash.com/documentation#get-a-photo).
 */
data class UnsplashPhoto(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("urls") val urls: UnsplashPhotoUrls,
    @field:SerializedName("user") val user: UnsplashUser
)
