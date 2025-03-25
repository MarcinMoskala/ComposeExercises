package com.marcinmoskala.composeexercises.ui.interop

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@Preview
@Composable
private fun ViewInCompose() {
    var radius by remember { mutableStateOf(100f) }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context -> // Must return a View
            MyCustomView(context).apply {
                setOnClickListener { radius += 10f }
            }
        },
        update = { view ->
            view.circleRadius = radius
        }
    )
}

class MyCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
        isAntiAlias = true // Smoother edges
    }

    var circleRadius = 50f  // Default radius
        set(value) {
            if(field == value) return
            field = value
            invalidate() // Request a redraw of the view
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        canvas.drawCircle(centerX, centerY, circleRadius, paint)

        paint.color = Color.BLUE // Change color for the text
        paint.textSize = 30f // Adjust text size as needed
        paint.textAlign = Paint.Align.CENTER // Center the text horizontally
        canvas.drawText(
            "Hello, MyCustomView!",
            centerX,  // X-coordinate (center)
            centerY + circleRadius + 40f, // Y-coordinate (slightly below the circle)
            paint
        )
        paint.color = Color.BLUE
    }
}