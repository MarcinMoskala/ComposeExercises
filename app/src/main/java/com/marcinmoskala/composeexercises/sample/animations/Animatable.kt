package com.marcinmoskala.composeexercises.sample.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Preview
@Composable
private fun AnimatablePreview() {
    val y = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            y.animateTo(200f)
            y.animateTo(0f)
            y.animateTo(400f)
            y.animateTo(0f)
            y.animateTo(800f)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            Modifier
                .offset { IntOffset(100, y.value.roundToInt()) }
                .background(color = Blue)
                .size(100.dp)
        )
    }
}

@Preview
@Composable
private fun AnimationPreview() {
    val anim = remember {
        TargetBasedAnimation(
            animationSpec = tween(2000),
            typeConverter = Int.VectorConverter,
            initialValue = 200,
            targetValue = 1000
        )
    }
    var y by remember { mutableIntStateOf(0) }

    LaunchedEffect(anim) {
        val startTime = withFrameNanos { it }

        do {
            val playTime = withFrameNanos { it } - startTime
            y = anim.getValueFromNanos(playTime)
        } while (y != 1000)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            Modifier
                .offset { IntOffset(100, y) }
                .background(color = Blue)
                .size(100.dp)
        )
    }
}