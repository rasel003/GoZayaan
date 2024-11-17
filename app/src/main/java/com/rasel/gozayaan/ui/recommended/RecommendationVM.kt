package com.rasel.gozayaan.ui.recommended

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rasel.gozayaan.data.HomeRepository
import com.rasel.gozayaan.data.models.RecommendationModel
import com.rasel.gozayaan.presentation.utils.CoroutineContextProvider
import com.rasel.gozayaan.presentation.utils.ExceptionHandler
import com.rasel.gozayaan.presentation.viewmodel.BaseViewModel
import com.rasel.gozayaan.presentation.viewmodel.CoroutinesErrorHandler
import com.rasel.gozayaan.util.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val homeRepository: HomeRepository
) : BaseViewModel(contextProvider) {

    private val _recommendedList: MutableLiveData<ApiResponse<List<RecommendationModel>>> = MutableLiveData()
    val recommendedList: LiveData<ApiResponse<List<RecommendationModel>>> get() = _recommendedList

    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        val message = ExceptionHandler.parse(exception)
    }

    fun getRecommendationList(
        coroutinesErrorHandler: CoroutinesErrorHandler
    ) = baseRequest(
        _recommendedList,
        coroutinesErrorHandler
    ) {
        homeRepository.getRecommendationList()
    }


}