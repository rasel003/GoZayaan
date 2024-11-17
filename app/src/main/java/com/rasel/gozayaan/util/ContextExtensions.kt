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

package com.rasel.gozayaan.util

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import es.dmoral.toasty.Toasty

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