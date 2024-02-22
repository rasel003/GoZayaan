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

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import es.dmoral.toasty.Toasty

/**
 * Retrieve a color from the current [android.content.res.Resources.Theme].
 */
@ColorInt
@SuppressLint("Recycle")
fun Context.themeColor(
    @AttrRes themeAttrId: Int
): Int {
    return obtainStyledAttributes(
        intArrayOf(themeAttrId)
    ).use {
        it.getColor(0, Color.MAGENTA)
    }
}

/** Reads the color attribute from the theme for given [colorAttributeId] */
fun Context.getColorFromTheme(colorAttributeId: Int): Int {
    val typedValue = TypedValue()
    val typedArray: TypedArray =
        this.obtainStyledAttributes(
            typedValue.data, intArrayOf(colorAttributeId)
        )
    @ColorInt val color = typedArray.getColor(0, 0)
    typedArray.recycle()
    return color
}

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}



fun Context.getRandomMaterialColor(typeColor: String): Int {
    var returnColor = Color.GRAY
    val arrayId = resources.getIdentifier("mdcolor_$typeColor", "array", packageName)

    if (arrayId != 0) {
        val colors = resources.obtainTypedArray(arrayId)
        val index = (Math.random() * colors.length()).toInt()
        returnColor = colors.getColor(index, Color.GRAY)
        colors.recycle()
    }
    return returnColor
}


/**
 * Retrieve a style from the current [android.content.res.Resources.Theme].
 */
@StyleRes
fun Context.themeStyle(@AttrRes attr: Int): Int {
    val tv = TypedValue()
    theme.resolveAttribute(attr, tv, true)
    return tv.data
}

@SuppressLint("Recycle")
fun Context.themeInterpolator(@AttrRes attr: Int): Interpolator {
    return AnimationUtils.loadInterpolator(
        this,
        obtainStyledAttributes(intArrayOf(attr)).use {
            it.getResourceId(0, android.R.interpolator.fast_out_slow_in)
        }
    )
}

fun Context.getDrawableOrNull(@DrawableRes id: Int?): Drawable? {
    return if (id == null || id == 0) null else AppCompatResources.getDrawable(this, id)
}

fun Context.showPermissionRequestDialog(
    title: String,
    body: String,
    callback: () -> Unit
) {
    AlertDialog.Builder(this).also {
        it.setTitle(title)
        it.setMessage(body)
        it.setPositiveButton("Ok") { _, _ ->
            callback()
        }
    }.create().show()
}

fun Context.permissionGranted(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Context.toastL(message: String) {
    Toasty.info(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastSystemL(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastSystemCopy(message: String) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.TOP, 0, 0);
    toast.show()
}

fun Context.toastS(message: String) {
    Toasty.info(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toastError(message: String) {
    Toasty.error(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toastWarning(message: String) {
    Toasty.warning(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastInfo(message: String) {
    Toasty.info(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toastLongInfo(message: String) {
    Toasty.info(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastSuccess(message: String) {
    Toasty.success(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastWarningLong(message: String) {
    Toasty.warning(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastSuccessLong(message: String) {
    Toasty.success(this, message, Toast.LENGTH_LONG).show()
}