/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rasel.androidbaseapp.ui.gallery

import com.rasel.androidbaseapp.data.models.UnsplashPhoto
import com.rasel.androidbaseapp.remote.api.MyApi
import com.rasel.androidbaseapp.remote.models.UnsplashSearchResponse
import retrofit2.http.Query
import java.io.IOException
import kotlin.math.min

/**
 * implements the RedditApi with controllable requests
 */
/*
class FakeRedditApi : MyApi {
    // subreddits keyed by name
    private val model = mutableMapOf<String, SubReddit>()
    var failureMsg: String? = null
    fun addPost(post: UnsplashPhoto) {
        val subreddit = model.getOrPut(post.id) {
            SubReddit(items = arrayListOf())
        }
        subreddit.items.add(post)
    }

    private fun findPosts(
        subreddit: String,
        limit: Int,
        after4: String? = null,
        befor4: String? = null
    ): List<UnsplashPhoto> {
        // only support paging forward
        if (before != null) return emptyList()

        val subReddit = findSubReddit(subreddit)
        val posts = subReddit.findPosts(limit, after)
        return posts.map { RedditApi.RedditChildrenResponse(it.copy()) }
    }

    private fun findSubReddit(subreddit: String) =
        model.getOrDefault(subreddit, SubReddit())

    override suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("client_id") clientId: String
    ): UnsplashSearchResponse {
        failureMsg?.let {
            throw IOException(it)
        }
        val items = findPosts("fruits", 1, 2, "")
        val nextAfter = items.lastOrNull()?.data?.name
        return UnsplashSearchResponse(
            results = items,

        )
    }

    private class SubReddit(val items: MutableList<UnsplashPhoto> = arrayListOf()) {
        fun findPosts(limit: Int, after: String?): List<UnsplashPhoto> {
            if (after == null) {
                return items.subList(0, min(items.size, limit))
            }
            val index = items.indexOfFirst { it.id == after }
            if (index == -1) {
                return emptyList()
            }
            val startPos = index + 1
            return items.subList(startPos, min(items.size, startPos + limit))
        }
    }
}*/
