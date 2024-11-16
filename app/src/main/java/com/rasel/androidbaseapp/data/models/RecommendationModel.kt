package com.rasel.androidbaseapp.data.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecommendationModel(
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("detail_image")
    val detailImage: String?,
    @SerializedName("fare")
    val fare: Int?,
    @SerializedName("fare_unit")
    val fareUnit: String?,
    @SerializedName("hero_image")
    val heroImage: String?,
    @SerializedName("is_available")
    val isAvailable: Boolean?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("property_name")
    val propertyName: String?,
    @SerializedName("rating")
    val rating: Double?
) : Parcelable