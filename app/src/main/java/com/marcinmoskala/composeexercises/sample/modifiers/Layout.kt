@file:Suppress("unused", "UNUSED_VARIABLE", "UNUSED_PARAMETER")

package com.marcinmoskala.composeexercises.sample.modifiers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import com.marcinmoskala.composeexercises.R

// Layout is one of the most powerful modifiers.
@Preview
@Composable
fun LayoutPreview() {
    Column {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Blue)
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .padding(10.dp)
//                .layout { measurable, constraints ->
//                    val newConstrains = constraints.offset(-20.dp.roundToPx(), -20.dp.roundToPx())
//                    val placeable = measurable.measure(newConstrains)
//                    val width = constraints.constrainWidth(placeable.width + 20.dp.roundToPx())
//                    val height = constraints.constrainHeight(placeable.height + 20.dp.roundToPx())
//                    layout(width, height) {
//                        placeable.place(10.dp.roundToPx(), 10.dp.roundToPx())
//                    }
//                }
//                .padding(10.dp)
//                .offset(10.dp, 10.dp)
//                .layout { measurable, constraints ->
//                    val placeable = measurable.measure(constraints)
//                    layout(constraints.maxWidth, constraints.maxHeight) {
//                        placeable.place(10.dp.roundToPx(), 10.dp.roundToPx())
//                    }
//                }
                .background(Green)
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Red)
        )
    }
}

@Preview
@Composable
private fun Padding() {
    Row {
        Image(
            painter = androidx.compose.ui.res.painterResource(id = R.drawable.avatar),
            contentDescription = null,
            modifier = androidx.compose.ui.Modifier
                .layout { measurable, constraints ->
                    val padding = 10.dp.roundToPx()
                    val newConstraints = constraints.offset(-padding * 2, -padding * 2)
                    val placeable = measurable.measure(newConstraints)
                    val width = constraints.constrainWidth(placeable.width + padding)
                    val height = constraints.constrainHeight(placeable.height + padding)
                    layout(width, height) {
                        placeable.placeRelative(padding, padding)
                    }
                }
        )
    }
}

@Preview
@Composable
private fun UnderstandingLayoutPhase() {
    Row {
        Image(
            painter = androidx.compose.ui.res.painterResource(id = R.drawable.avatar),
            contentDescription = null,
            modifier = androidx.compose.ui.Modifier
                .layout { measurable, constraints ->
                    println("#1 Need to measure child")
                    val placeable = measurable.measure(constraints)
                    println("#1 Child measured, specifying layout")
                    layout(placeable.width, placeable.height) {
                        println("#1 Placing")
                        placeable.place(0, 0)
                    }
                }
                .layout { measurable, constraints ->
                    println("#2 Need to measure child")
                    val placeable = measurable.measure(constraints)
                    println("#2 Child measured, specifying layout")
                    layout(placeable.width, placeable.height) {
                        println("#2 Placing")
                        placeable.place(0, 0)
                    }
                }
        )
    }
}