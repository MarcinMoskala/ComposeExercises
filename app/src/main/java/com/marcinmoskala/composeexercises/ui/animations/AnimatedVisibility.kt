package com.marcinmoskala.composeexercises.ui.animations

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun AnimatedVisibilityPreview() {
    var middleVisible by remember { mutableStateOf(true) }
    val boxModifiers = Modifier
        .padding(4.dp)
        .size(100.dp)
        .border(4.dp, Color.Black)
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = boxModifiers)
        AnimatedVisibility(middleVisible) {
            Box(modifier = boxModifiers)
        }
        Box(modifier = boxModifiers)
        Button(
            onClick = { middleVisible = !middleVisible },
            colors = buttonColors(containerColor = if (middleVisible) Color.Unspecified else Color.Transparent)
        ) {
            Text(
                "Toggle middle",
                color = if (middleVisible) Color.White else Color.Black
            )
        }
    }
}

@Preview
@Composable
fun AnimatedVisibilityGame() {
    var switch1 by remember { mutableStateOf(true) }
    var switch2 by remember { mutableStateOf(true) }
    var switch3 by remember { mutableStateOf(true) }

    Column {
        Row {
            AnimatedVisibility(switch1 && switch2 || !switch3) {
                Text(
                    "D1",
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Red)
                )
            }
            AnimatedVisibility(!switch1 || switch2 && switch3) {
                Text(
                    "D2",
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Blue)
                )
            }
        }
        Row {
            AnimatedVisibility(!switch1 || switch2) {
                Text(
                    "D3",
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Green)
                )
            }
            AnimatedVisibility(switch2 || !switch3) {
                Text(
                    "D4",
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Yellow)
                )
            }
        }

        Button(
            onClick = { switch1 = !switch1 },
            colors = buttonColors(containerColor = if (switch1) Color.Unspecified else Color.Transparent)
        ) {
            Text(
                "Switch 1",
                color = if (switch1) Color.White else Color.Black
            )
        }
        Button(
            onClick = { switch2 = !switch2 },
            colors = buttonColors(containerColor = if (switch2) Color.Unspecified else Color.Transparent)
        ) {
            Text(
                "Switch 2",
                color = if (switch2) Color.White else Color.Black
            )
        }
        Button(
            onClick = { switch3 = !switch3 },
            colors = buttonColors(containerColor = if (switch3) Color.Unspecified else Color.Transparent)
        ) {
            Text(
                "Switch 3",
                color = if (switch3) Color.White else Color.Black
            )
        }
    }
}

@Preview
@Composable
fun AnimatedVisibilityTransformationsPreview() {
    var counter by remember { mutableStateOf(0) }
    val boxModifiers = Modifier
        .padding(4.dp)
        .size(150.dp)
        .border(4.dp, Color.Black)
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = (counter - 1) % 8 != 0,
        ) {
            Box(modifier = boxModifiers.background(Color.Blue))
        }
        AnimatedVisibility(
            visible = (counter - 3) % 8 != 0,
            enter = scaleIn(tween(2000)),
            exit = scaleOut(tween(2000))
        ) {
            Box(modifier = boxModifiers.background(Color.Red))
        }
        AnimatedVisibility(
            visible = (counter - 5) % 8 != 0,
            enter = expandIn(tween(2000)), // see , clip = false
            exit = shrinkOut(tween(2000))
        ) {
            Box(modifier = boxModifiers.background(Color.Green))
        }
        AnimatedVisibility(
            visible = (counter - 7) % 8 != 0,
            enter = slideIn(tween(2000), initialOffset = { IntOffset(it.height, it.width) }),
            exit = slideOut(tween(2000), targetOffset = { IntOffset(it.height, it.width) })
        ) {
            Box(modifier = boxModifiers.background(Color.Magenta))
        }
        Button(onClick = { counter++ }) {
            Text("Next")
        }
    }
}