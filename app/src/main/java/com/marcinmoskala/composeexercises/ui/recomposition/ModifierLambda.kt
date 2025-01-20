package com.marcinmoskala.composeexercises.ui.recomposition

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun AnimatedOffset() {
    var isOffset by remember { mutableStateOf(false) }

    val offset by animateIntOffsetAsState(
        targetValue = if (isOffset) {
            IntOffset(700, 700)
        } else {
            IntOffset(0, 0)
        },
        animationSpec = tween(2000)
    )

    Box(
        modifier = Modifier.fillMaxSize()
            .clickable { isOffset = !isOffset }
    ) {
        Box(
            modifier = Modifier
//                .offset { offset }
                .offset(offset.x.dp, offset.y.dp)
                .size(100.dp)
                .background(Color.Blue)
        )
    }
}