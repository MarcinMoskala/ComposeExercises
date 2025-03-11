package com.marcinmoskala.composeexercises.ui.modifiers

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun PulsatingButtonPreview() {
    val animatedColor by rememberInfiniteTransition()
        .animateColor(
            initialValue = Color.Blue,
            targetValue = Color.Blue.copy(alpha = 0.3f),
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

    Text(
        text = "Click me",
        modifier = Modifier
            .padding(8.dp)
            .drawBehind {
                drawRect(
                    color = animatedColor,
                    style = Stroke(width = 4.dp.toPx()),
                    size = size
                )
            }
            .padding(8.dp)
    )
}