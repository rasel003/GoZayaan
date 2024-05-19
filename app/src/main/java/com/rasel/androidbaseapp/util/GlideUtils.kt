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
        size: Int = 13,
        isRandom: Boolean = true
    ): List<TitleAndId> {
        val characters = mutableListOf<TitleAndId>()
        if (isRandom) {
            repeat(size) {
                characters.add(TitleAndId(title = getFixedImageList().random(), id = it))
            }
        } else {
            getFixedImageList().forEachIndexed { index, link ->
                characters.add(TitleAndId(title = link, id = index))
            }
        }
        return characters
    }

    fun getFixedImageList(): List<String> {

        val smallImageList = listOf(
            "https://images.unsplash.com/photo-1619566636858-adf3ef46400b?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzfHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NTN8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1550258987-190a2d41a8ba?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxfHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NTJ8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1584559582128-b8be739912e1?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxMHx8RnJ1aXR8ZW58MHx8fHwxNzEzODEwOTUzfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1536063211352-0b94219f6212?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxfHxiZWF1dGlmdWwlMjBnaXJsfGVufDB8fHx8MTcxNTk5MTQ5NXww&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1561742139-77a5a42cc97b?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyfHxiZWF1dGlmdWwlMjBnaXJsfGVufDB8fHx8MTcxNTk5MTQ5NXww&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1601004890684-d8cbf643f5f2?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyfHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NTJ8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1519282407670-6e0ace7ffc96?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0fHxiZWF1dGlmdWwlMjBnaXJsfGVufDB8fHx8MTcxNTk5MTQ5NXww&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1528825871115-3581a5387919?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw3fHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NTN8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1600423115367-87ea7661688f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw5fHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NTN8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/flagged/photo-1571837360114-edf5dba7b1dd?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzfHxiZWF1dGlmdWwlMjBnaXJsfGVufDB8fHx8MTcxNTk5MTQ5NXww&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1516726817505-f5ed825624d8?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw1fHxiZWF1dGlmdWwlMjBnaXJsfGVufDB8fHx8MTcxNTk5MTQ5NXww&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1596245195341-b33a7f275fdb?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw2fHxiZWF1dGlmdWwlMjBnaXJsfGVufDB8fHx8MTcxNTk5MTQ5NXww&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1577234286642-fc512a5f8f11?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw4fHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NTN8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1542027959157-98e6745f4ba7?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw3fHxiZWF1dGlmdWwlMjBnaXJsfGVufDB8fHx8MTcxNTk5MTQ5NXww&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1515138692129-197a2c608cfd?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw4fHxiZWF1dGlmdWwlMjBnaXJsfGVufDB8fHx8MTcxNTk5MTQ5NXww&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1534794420636-dbc13b8a48d2?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw5fHxiZWF1dGlmdWwlMjBnaXJsfGVufDB8fHx8MTcxNTk5MTQ5NXww&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1527189919029-aeb3d997547d?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxMHx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE0OTV8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1532597883259-4b593010e4ce?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxMXx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE0OTV8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1550935114-99de2f488f47?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxMnx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE0OTV8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1505635725851-c2cfe9e29112?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxM3x8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE0OTV8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1606814893907-c2e42943c91f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxNHx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE0OTV8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1515049211820-b3adaa5f3d19?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxNXx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE0OTV8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1509205206130-24819154d9e8?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxNnx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE0OTV8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1557800636-894a64c1696f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0fHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NTN8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1574856344991-aaa31b6f4ce3?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw1fHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NTN8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1582979512210-99b6a53386f9?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw2fHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NTN8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1482482097755-0b595893ba63?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxN3x8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE0OTV8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1631947430066-48c30d57b943?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxOHx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE0OTV8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1527692282582-538da08c9d00?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxOXx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE0OTV8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1522767131594-6b7e96848fba?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyMHx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE0OTV8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1613005798967-632017e477c8?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyMXx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE0OTV8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1505919130581-e235c7ed874f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyMnx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE0OTV8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1563987219716-dac41f2d0b3a?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyM3x8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE0OTV8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1516029637308-3adce832dbec?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyNHx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE0OTV8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1542027916873-a55e9776fb34?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyNXx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE0OTV8MA&ixlib=rb-4.0.3&q=80&w=400]",
            "https://images.unsplash.com/photo-1447338065307-fbe2a1416586?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyNnx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1519993025985-1154cd780552?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyN3x8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1518743166649-e979d588c204?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyOHx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1600107215508-1886e6e49c11?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyOXx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1567684014529-98613a746dd2?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzMHx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1518743761921-f0c62f72cf6d?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzMXx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1522765438697-bbbcc76e8614?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzMnx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1563178406-4cdc2923acbc?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzM3x8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1547580385-8ac494262c86?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzNHx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1524668115671-4b531e88b22e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzNXx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1600938401159-0a419a66b5f3?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzNnx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1558888522-a0172835ccd7?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzN3x8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1510696089168-f1d31149bdde?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzOHx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1609827390495-3c9adc0b3ca9?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzOXx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1531847940878-66b8fa862eeb?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0MHx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1520466809213-7b9a56adcd45?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0MXx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1613365765813-e29fea16ecaa?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0Mnx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1533562191720-216371fd425a?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0M3x8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1601832823286-f4dff34eebc7?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0NHx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1517924030062-84f2c8f0784e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0NXx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1552298930-24a24595de10?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0Nnx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1516575150278-77136aed6920?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0N3x8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1512646846467-59c526586e79?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0OHx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1521468808608-ffa7a1927f6d?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0OXx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1502808777105-c5f7b639feae?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw1MHx8YmVhdXRpZnVsJTIwZ2lybHxlbnwwfHx8fDE3MTU5OTE1MjF8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1527736947477-2790e28f3443?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxfHxob3QlMjBnaXJsfGVufDB8fHx8MTcxNjA0MDU2NHww&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1522262590532-a991489a0253?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyfHxob3QlMjBnaXJsfGVufDB8fHx8MTcxNjA0MDU2NHww&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1527199372136-dff50c10ea34?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzfHxob3QlMjBnaXJsfGVufDB8fHx8MTcxNjA0MDU2NHww&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1536811145290-bc394643e51e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0fHxob3QlMjBnaXJsfGVufDB8fHx8MTcxNjA0MDU2NHww&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1520694977332-9122aa8e8b7a?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw1fHxob3QlMjBnaXJsfGVufDB8fHx8MTcxNjA0MDU2NHww&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1527189919029-aeb3d997547d?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw2fHxob3QlMjBnaXJsfGVufDB8fHx8MTcxNjA0MDU2NHww&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1527627964160-fc5665e5a374?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw3fHxob3QlMjBnaXJsfGVufDB8fHx8MTcxNjA0MDU2NHww&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1536763225213-b5592b525630?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw4fHxob3QlMjBnaXJsfGVufDB8fHx8MTcxNjA0MDU2NHww&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1495296231482-75620171f758?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw5fHxob3QlMjBnaXJsfGVufDB8fHx8MTcxNjA0MDU2NHww&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1527817961873-3fb213da804d?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxMHx8aG90JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDA1NjR8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1502767882403-636aee14f873?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxfHxnb3JnZW91cyUyMGFkdWx0JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDI3ODZ8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1505200063777-4a0b90b007c8?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyfHxnb3JnZW91cyUyMGFkdWx0JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDI3ODZ8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1494354205675-139c8101dfa5?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzfHxnb3JnZW91cyUyMGFkdWx0JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDI3ODZ8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1680721698116-2243a1221ac0?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0fHxnb3JnZW91cyUyMGFkdWx0JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDI3ODZ8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1548858806-e064cf9872c0?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw1fHxnb3JnZW91cyUyMGFkdWx0JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDI3ODd8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605616253-0212f3ea5304?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw2fHxnb3JnZW91cyUyMGFkdWx0JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDI3ODd8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1680721698119-171cb3a50816?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw3fHxnb3JnZW91cyUyMGFkdWx0JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDI3ODd8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1613800813208-07d40446574e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw4fHxnb3JnZW91cyUyMGFkdWx0JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDI3ODd8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605617176-c263b5f9e1de?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw5fHxnb3JnZW91cyUyMGFkdWx0JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDI3ODd8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605615996-fc371ef2ad99?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxMHx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyNzg3fDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1502767882403-636aee14f873?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxfHxnb3JnZW91cyUyMGFkdWx0JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDI3ODd8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1505200063777-4a0b90b007c8?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyfHxnb3JnZW91cyUyMGFkdWx0JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDI3ODd8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1494354205675-139c8101dfa5?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzfHxnb3JnZW91cyUyMGFkdWx0JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDI3ODd8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1680721698116-2243a1221ac0?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0fHxnb3JnZW91cyUyMGFkdWx0JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDI3ODd8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1548858806-e064cf9872c0?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw1fHxnb3JnZW91cyUyMGFkdWx0JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDI3ODd8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605616253-0212f3ea5304?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw2fHxnb3JnZW91cyUyMGFkdWx0JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDI3ODd8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1680721698119-171cb3a50816?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw3fHxnb3JnZW91cyUyMGFkdWx0JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDI3ODd8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1613800813208-07d40446574e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw4fHxnb3JnZW91cyUyMGFkdWx0JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDI3ODd8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605617176-c263b5f9e1de?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw5fHxnb3JnZW91cyUyMGFkdWx0JTIwZ2lybHxlbnwwfHx8fDE3MTYwNDI3ODd8MA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605615996-fc371ef2ad99?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxMHx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyNzg3fDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605617319-eab10b4a8954?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxMXx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyNzg3fDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605617209-8017e4b7943d?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxMnx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyNzg3fDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605617000-f9067ca66537?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxM3x8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyNzg3fDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605617324-2730bd909dd4?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxNHx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyNzg3fDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605615764-e6379795e1b6?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxNXx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyNzg3fDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605615726-82f5c5d84a8b?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxNnx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyNzg3fDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605616987-65de166da4fd?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxN3x8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyNzg3fDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605617257-7ce79981dce5?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxOHx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyNzg3fDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605617412-ad6d6c02133e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxOXx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyNzg3fDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1619498561578-161d06083968?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyMHx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyNzg3fDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605617453-dabd8ddd933c?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyMXx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyNzg3fDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605617365-9c22d1b3c5cd?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyMnx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyNzg3fDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1619255973877-c3e608171aee?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyM3x8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyNzg3fDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605617435-19e34574f3cf?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyNHx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyNzg3fDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1613800813591-91cd09c5f39f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyNXx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyNzg3fDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605617240-790ed3129a3d?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyNnx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1591814501760-e4c6c8379329?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyN3x8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1613800813061-f12546b23583?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyOHx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1619255973026-ce92235804b5?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwyOXx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1636814436182-2a6717b0119b?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzMHx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1636814435308-9f751e33b1b3?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzMXx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1619255972153-c7aec75fed53?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzMnx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1636814435311-7a1468724e46?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzM3x8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1613800812017-f4cb202ec15e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzNHx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1636814435775-716790f6fcea?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzNXx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1619498561020-5242a91edbb8?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzNnx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1636814434187-311f173586c5?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzN3x8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1613800811878-3ef4c86da350?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzOHx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1652114439641-948648e0624a?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwzOXx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1518904659698-a5ec9180760f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0MHx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1605572676841-17a95717fca1?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0MXx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1619255974277-69d6a8a9d47a?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0Mnx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605617229-bf514086e454?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0M3x8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1619255974091-4b6dc7c52a57?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0NHx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1636814435315-3de270f38a89?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0NXx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1619498563461-231f15670c4b?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0Nnx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1619255974370-67d9cb8535f4?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0N3x8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1610629737680-8a6daf3dadf7?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0OHx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1634605617303-449dc8d89bbb?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw0OXx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400",
            "https://images.unsplash.com/photo-1619498561400-3a66d4173c60?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHw1MHx8Z29yZ2VvdXMlMjBhZHVsdCUyMGdpcmx8ZW58MHx8fHwxNzE2MDQyODYyfDA&ixlib=rb-4.0.3&q=80&w=400"

        )

        val largeImageList = listOf(
            "https://images.unsplash.com/photo-1550258987-190a2d41a8ba?crop=entropy&cs=srgb&fm=jpg&ixid=M3wxNTYwMDZ8MHwxfHNlYXJjaHwxfHxGcnVpdHxlbnwwfHx8fDE3MTM4MTA5NjJ8MA&ixlib=rb-4.0.3&q=85",
        )

        return smallImageList
    }
}
