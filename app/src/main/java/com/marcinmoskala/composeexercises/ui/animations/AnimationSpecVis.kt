package com.marcinmoskala.composeexercises.ui.animations

import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring.StiffnessHigh
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.keyframesWithSpline
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.drawText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationSpecApi::class)
@Preview(device = "spec:parent=pixel_tablet,orientation=portrait")
@Composable
private fun AnimationSpecVisPreview() {
    val screenWidth = LocalContext.current.resources.displayMetrics.widthPixels
    var drawing by remember { mutableStateOf(true) }
    var target by remember { mutableStateOf(200f) }
    val transition = updateTransition(target)
    val x1 by transition.animateFloat(transitionSpec = {
        tween(
            2000,
            easing = FastOutSlowInEasing
        )
    }) { it }
    val x2 by transition.animateFloat(transitionSpec = {
        tween(
            2000,
            easing = LinearOutSlowInEasing
        )
    }) { it }
    val x3 by transition.animateFloat(transitionSpec = {
        tween(
            2000,
            easing = FastOutLinearInEasing
        )
    }) { it }
    val x4 by transition.animateFloat(transitionSpec = {
        tween(
            2000,
            easing = LinearEasing
        )
    }) { it }
    val x5 by transition.animateFloat(transitionSpec = { spring(stiffness = 20f) }) { it }
    val fourth = (screenWidth.toFloat() - 200) / 4
    val half = fourth * 2
    val x6 by transition.animateFloat(transitionSpec = {
        keyframes {
            durationMillis = 2000
            200f at 0 using LinearOutSlowInEasing // for 0-15 ms
            (200f + fourth) atFraction 0.25f using FastOutLinearInEasing // for 15-75 ms
            (200f + half) atFraction 0.5f
            (200f + half + fourth) atFraction 0.75f
        }
    }) { it }
    val x7 by transition.animateFloat(transitionSpec = {
        keyframes {
            durationMillis = 2000
            200f at 0
            600f at 500
            650f at 1000
            1000f at 1500
            1050f at 1800
        }
    }) { it }
    val x8 by transition.animateFloat(transitionSpec = {
        keyframesWithSpline {
            durationMillis = 2000
            200f at 0
            600f at 500
            650f at 1000
            1000f at 1500
            1050f at 1800
        }
    }) { it }
    var x1History by remember { mutableStateOf(listOf<Float>()) }
    var x2History by remember { mutableStateOf(listOf<Float>()) }
    var x3History by remember { mutableStateOf(listOf<Float>()) }
    var x4History by remember { mutableStateOf(listOf<Float>()) }
    var x5History by remember { mutableStateOf(listOf<Float>()) }
    var x6History by remember { mutableStateOf(listOf<Float>()) }
    var x7History by remember { mutableStateOf(listOf<Float>()) }
    var x8History by remember { mutableStateOf(listOf<Float>()) }

    LaunchedEffect(Unit) {
        target = screenWidth.toFloat() - 200
        val x1HistoryTemp = mutableListOf<Float>()
        val x2HistoryTemp = mutableListOf<Float>()
        val x3HistoryTemp = mutableListOf<Float>()
        val x4HistoryTemp = mutableListOf<Float>()
        val x5HistoryTemp = mutableListOf<Float>()
        val x6HistoryTemp = mutableListOf<Float>()
        val x7HistoryTemp = mutableListOf<Float>()
        val x8HistoryTemp = mutableListOf<Float>()
        repeat(200) {
            x1HistoryTemp += x1
            x2HistoryTemp += x2
            x3HistoryTemp += x3
            x4HistoryTemp += x4
            x5HistoryTemp += x5
            x6HistoryTemp += x6
            x7HistoryTemp += x7
            x8HistoryTemp += x8
            delay(10)
        }
        x1History = x1HistoryTemp
        x2History = x2HistoryTemp
        x3History = x3HistoryTemp
        x4History = x4HistoryTemp
        x5History = x5HistoryTemp
        x6History = x6HistoryTemp
        x7History = x7HistoryTemp
        x8History = x8HistoryTemp
        drawing = false
    }
    Box(
        contentAlignment = Alignment.TopCenter,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (drawing) {
                drawCircle(Color.Red, radius = 50f, center = Offset(x1, 240f))
                drawCircle(Color.Red, radius = 50f, center = Offset(x2, 480f))
                drawCircle(Color.Red, radius = 50f, center = Offset(x3, 720f))
                drawCircle(Color.Red, radius = 50f, center = Offset(x4, 960f))
                drawCircle(Color.Red, radius = 50f, center = Offset(x5, 1200f))
                drawCircle(Color.Red, radius = 50f, center = Offset(x6, 1570f))
                drawCircle(Color.Red, radius = 50f, center = Offset(x7, 1940f))
                drawCircle(Color.Red, radius = 50f, center = Offset(x8, 2350f))
            } else {
                x1History.forEachIndexed { index, x ->
                    drawCircle(
                        Color.Black,
                        radius = 5f,
                        center = Offset(x, 720f)
                    )
                }
                x2History.forEachIndexed { index, x ->
                    drawCircle(
                        Color.Black,
                        radius = 5f,
                        center = Offset(x, 480f)
                    )
                }
                x3History.forEachIndexed { index, x ->
                    drawCircle(
                        Color.Black,
                        radius = 5f,
                        center = Offset(x, 720f)
                    )
                }
                x4History.forEachIndexed { index, x ->
                    drawCircle(
                        Color.Black,
                        radius = 5f,
                        center = Offset(x, 960f)
                    )
                }
                x5History.forEachIndexed { index, x ->
                    drawCircle(
                        Color.Black,
                        radius = 5f,
                        center = Offset(x, 1200f)
                    )
                }
                x6History.forEachIndexed { index, x ->
                    drawCircle(
                        Color.Black,
                        radius = 5f,
                        center = Offset(x, 1570f)
                    )
                }
                x7History.forEachIndexed { index, x ->
                    drawCircle(
                        Color.Black,
                        radius = 5f,
                        center = Offset(x, 1940f)
                    )
                }
                x8History.forEachIndexed { index, x ->
                    drawCircle(
                        Color.Black,
                        radius = 5f,
                        center = Offset(x, 2350f)
                    )
                }
//        }
            }
        }
        Text(
            "tween(easing = FastOutSlowInEasing)",
            fontSize = 20.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 50.dp)
        )
        Text(
            "tween(easing = LinearOutSlowInEasing)",
            fontSize = 20.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 170.dp)
        )
        Text(
            "tween(easing = FastOutLinearInEasing)",
            fontSize = 20.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 285.dp)
        )
        Text(
            "tween(easing = LinearEasing)",
            fontSize = 20.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 405.dp)
        )
        Text(
            "spring(stiffness = 20f)",
            fontSize = 20.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 530.dp)
        )
        Text(
            """
                keyframes {
                    200f at 0 using LinearOutSlowInEasing
                    (200f + fourth) atFraction 0.25f using FastOutLinearInEasing
                    (200f + half) atFraction 0.5f
                    (200f + half + fourth) atFraction 0.75f
                }
            """.trimIndent(),
            fontSize = 12.sp,
            modifier = Modifier
                .offset(y = 648.dp)
        )
        Text(
            """
                keyframes {
                    200f at 0
                    600f at 500
                    650f at 1000
                    1000f at 1500
                    1050f at 1800
                }
            """.trimIndent(),
            fontSize = 12.sp,
            modifier = Modifier
                .offset(y = 820.dp)
        )
        Text(
            """
                keyframesWithSpline {
                    200f at 0
                    600f at 500
                    650f at 1000
                    1000f at 1500
                    1050f at 1800
                }
            """.trimIndent(),
            fontSize = 12.sp,
            modifier = Modifier
                .offset(y = 1020.dp)
        )
    }
}

