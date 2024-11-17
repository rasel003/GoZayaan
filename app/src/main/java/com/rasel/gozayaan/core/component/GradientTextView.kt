package com.rasel.gozayaan.core.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.rasel.gozayaan.R

class GradientTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var startColor: Int = 0xFFE91E63.toInt() // Default color (Pink)
    private var endColor: Int = 0xFF2196F3.toInt()   // Default color (Blue)
    private var gradient: LinearGradient? = null
    private var gradientInitialized = false

    init {
        // Load custom attributes
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GradientTextView,
            0, 0
        ).apply {
            try {
                // Fetch custom colors, if defined
                startColor = getColor(R.styleable.GradientTextView_startColor, startColor)
                endColor = getColor(R.styleable.GradientTextView_endColor, endColor)
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (!gradientInitialized) {
            val textWidth = paint.measureText(text.toString())
            gradient = LinearGradient(
                0f, 0f, textWidth, 0f,
                intArrayOf(startColor, endColor),
                null,
                Shader.TileMode.CLAMP
            )
            paint.shader = gradient
            gradientInitialized = true
        }

        super.onDraw(canvas)
    }

    // Optional method to update colors dynamically if needed
    fun setGradientColors(startColor: Int, endColor: Int) {
        this.startColor = startColor
        this.endColor = endColor
        gradientInitialized = false
        invalidate()
    }
}
