package com.marcinmoskala.composeexercises.sample.animations

import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.marcinmoskala.composeexercises.R
import kotlin.math.roundToInt

@Preview
@Composable
fun RunningCompose() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    data class Position(val x: Dp, val y: Dp)
    val transition = rememberInfiniteTransition()
    val location by transition.animateValue(
        initialValue = Position(screenWidth / 2 - 50.dp, 0.dp),
        targetValue = Position(screenWidth / 2 - 50.dp, screenHeight - 100.dp),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = TwoWayConverter<Position, AnimationVector2D>(
            convertToVector = { AnimationVector2D(it.x.value, it.y.value) },
            convertFromVector = { Position(Dp(it.v1), Dp(it.v2)) }
        )
    )
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.compose),
            contentDescription = "Runner",
            modifier = Modifier
                .size(100.dp)
                .offset { IntOffset(location.x.toPx().roundToInt(), location.y.toPx().roundToInt()) }
        )
    }
}