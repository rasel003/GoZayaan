package com.rasel.androidbaseapp.util

interface UiModel

open class UiAwareModel : UiModel {
    var isRedelivered: Boolean = false
}
