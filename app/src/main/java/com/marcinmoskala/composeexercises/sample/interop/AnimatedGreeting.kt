package com.marcinmoskala.composeexercises.sample.interop

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marcinmoskala.composeexercises.R
import kotlin.math.cos
import kotlin.math.sin

@Preview
@Composable
fun AnimatedGreeting(text: String = "from Compose!") {
    val animation = rememberInfiniteTransition()
    val offsetProgress by animation.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val alpha by animation.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        contentAlignment = androidx.compose.ui.Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer {
                // Make circle movement
                translationX = 100 * cos(offsetProgress * 2 * Math.PI).toFloat()
                translationY = 100 * sin(offsetProgress * 2 * Math.PI).toFloat()
                this.alpha = sin(alpha * Math.PI).toFloat() * 0.5f + 0.5f
            }
        ) {
            Image(
                painter = painterResource(R.drawable.compose),
                contentDescription = "Android Logo",
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = "Hello $text",
                fontSize = 24.sp,
            )
        }
    }
}