package com.aqube.ram.core.components

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.google.android.material.switchmaterial.SwitchMaterial
import com.rasel.androidbaseapp.R

class RMSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SwitchMaterial(context, attrs) {

    init {
        thumbDrawable = getDrawable(context, R.drawable.selector_dark_light)
        trackDrawable = getDrawable(context, R.drawable.selector_bg_dark_light)
    }
}
