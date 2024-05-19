package com.rasel.androidbaseapp.ui.gallery

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rasel.androidbaseapp.remote.api.MyApi
import com.rasel.androidbaseapp.data.models.UnsplashPhoto
import timber.log.Timber

private const val UNSPLASH_STARTING_PAGE_INDEX = 0

class UnsplashPagingSource(
    private val api: MyApi,
    private val query: String
) : PagingSource<Int, UnsplashPhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        return try {
            val response = api.searchPhotos(query, page, params.loadSize)
            val photos = response.results

            Timber.tag("rsl").d("api called query: $query, page: $page, params: $params")
            Timber.tag("rsl").d("response: ${response.results.map { it.urls.small }}")

            LoadResult.Page(
                data = photos,
                prevKey = if (page == UNSPLASH_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page == response.totalPages) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>): Int? {
        return 0
    }
}
