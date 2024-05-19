package com.rasel.androidbaseapp.data

import com.rasel.androidbaseapp.data.models.TitleAndId
import com.rasel.androidbaseapp.util.ApiResponse
import com.rasel.androidbaseapp.util.FakeValueFactory
import com.rasel.androidbaseapp.util.apiRequestFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class GalleryRepositoryImp @Inject constructor(

) {

    suspend fun getImageList(): Flow<List<TitleAndId>> = flow {

//        Timber.tag(TAG).d("GalleryRepositoryImp getImageList: get called")
        val data = FakeValueFactory.getImageList(isRandom = false)

        emit(data)
    }

    fun getImageList2() : Flow<ApiResponse<List<TitleAndId>>> =  flow  {

//        Timber.tag(TAG).d("GalleryRepositoryImp getImageList: get called again")
        val data = FakeValueFactory.getImageList(isRandom = false)


       emit(  ApiResponse.Success(data) )
    }

    companion object {
        private const val TAG = "rsl"
    }
}
