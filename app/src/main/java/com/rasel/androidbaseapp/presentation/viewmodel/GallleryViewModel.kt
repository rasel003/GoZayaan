package com.rasel.androidbaseapp.presentation.viewmodel

import com.rasel.androidbaseapp.data.models.TitleAndId
import com.rasel.androidbaseapp.presentation.utils.UiAwareModel

private const val TAG = "rsl"

sealed class GalleryUIModel : UiAwareModel() {
    object Loading : GalleryUIModel()
    data class Error(var error: String = "") : GalleryUIModel()
    data class Success(val data: List<TitleAndId>) : GalleryUIModel()
}

