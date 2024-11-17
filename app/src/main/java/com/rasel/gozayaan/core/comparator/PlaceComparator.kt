package com.rasel.gozayaan.core.comparator

import androidx.recyclerview.widget.DiffUtil
import com.rasel.gozayaan.data.models.RecommendationModel

class PlaceComparator : DiffUtil.ItemCallback<RecommendationModel>() {

    override fun areItemsTheSame(
        oldItem: RecommendationModel,
        newItem: RecommendationModel
    ): Boolean {
        return oldItem.propertyName == newItem.propertyName
    }

    override fun areContentsTheSame(
        oldItem: RecommendationModel,
        newItem: RecommendationModel
    ): Boolean {
        return oldItem == newItem
    }
}