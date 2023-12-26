
package com.rasel.androidbaseapp.data.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Data class that represents a photo from Unsplash.
 *
 * Not all of the fields returned from the API are represented here; only the ones used in this
 * project are listed below. For a full list of fields, consult the API documentation
 * [here](https://unsplash.com/documentation#get-a-photo).
 */


@Parcelize
data class UnsplashPhoto (
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("urls")
    val urls: UnsplashPhotoUrls,
    @field:SerializedName("user")
    val user: UnsplashUser
) : Parcelable
