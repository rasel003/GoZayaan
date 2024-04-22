/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rasel.androidbaseapp.util

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rasel.androidbaseapp.data.models.TitleAndId
import java.util.*
import kotlin.random.Random

fun loadListener(block: (loaded: Boolean) -> Unit) = GlideDrawableLoadListener(block)

/**
 * A [RequestListener] which executes an action when a [Drawable] loads or fails to load.
 */
class GlideDrawableLoadListener(private val block: (loaded: Boolean) -> Unit) :
    RequestListener<Drawable> {

    override fun onResourceReady(
        resource: Drawable,
        model: Any,
        target: Target<Drawable>?,
        dataSource: DataSource,
        isFirstResource: Boolean
    ): Boolean {
        block(true)
        return false
    }

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>,
        isFirstResource: Boolean
    ): Boolean {
        block(false)
        return true
    }
}

object FakeValueFactory {

    fun randomString(): String {
        return UUID.randomUUID().toString()
    }

    fun randomInt(): Int {
        return Random.nextInt()
    }

    fun randomBoolean(): Boolean {
        return Random.nextBoolean()
    }

    fun getImageList(
        size: Int
    ): List<TitleAndId> {
        val characters = mutableListOf<TitleAndId>()
        repeat(size) {

            characters.add(TitleAndId(title = getRandomImage(), id = it))
        }
        return characters
    }

    fun getRandomImage(): String {

        val smallImageList = listOf( "https://images.unsplash.com/photo-1550258987-190a2d41a8ba?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxfHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NTJ8MA&ixlib=rb-4.0.3&q=80&w=400",
             "https://images.unsplash.com/photo-1601004890684-d8cbf643f5f2?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyfHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NTJ8MA&ixlib=rb-4.0.3&q=80&w=400",
             "https://images.unsplash.com/photo-1619566636858-adf3ef46400b?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzfHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NTN8MA&ixlib=rb-4.0.3&q=80&w=400",
             "https://images.unsplash.com/photo-1557800636-894a64c1696f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0fHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NTN8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1574856344991-aaa31b6f4ce3?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw1fHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NTN8MA&ixlib=rb-4.0.3&q=80&w=400",
             "https://images.unsplash.com/photo-1582979512210-99b6a53386f9?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw2fHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NTN8MA&ixlib=rb-4.0.3&q=80&w=400",
             "https://images.unsplash.com/photo-1528825871115-3581a5387919?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw3fHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NTN8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1577234286642-fc512a5f8f11?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw4fHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NTN8MA&ixlib=rb-4.0.3&q=80&w=400",
             "https://images.unsplash.com/photo-1600423115367-87ea7661688f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw5fHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NTN8MA&ixlib=rb-4.0.3&q=80&w=400",
             "https://images.unsplash.com/photo-1584559582128-b8be739912e1?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxMHx8RnJ1aXR8ZW58MHx8fHwxNzEzODEwOTUzfDA&ixlib=rb-4.0.3&q=80&w=400")

       val largeImageList = listOf(
           "https://images.unsplash.com/photo-1550258987-190a2d41a8ba?crop=entropy&cs=srgb&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxfHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NjJ8MA&ixlib=rb-4.0.3&q=85",
       )

        return smallImageList.random()

    }
}
