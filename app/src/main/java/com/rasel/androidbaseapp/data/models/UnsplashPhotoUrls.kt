package com.rasel.androidbaseapp.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Data class that represents URLs available for a Unsplash photo.
 *
 * Although several photo sizes are available, this project uses only uses the `small` sized photo.
 * For more details, consult the API documentation
 * [here](https://unsplash.com/documentation#example-image-use).
 */

@Parcelize
data class UnsplashPhotoUrls(
    @field:SerializedName("small") val small: String
) : Parcelable
