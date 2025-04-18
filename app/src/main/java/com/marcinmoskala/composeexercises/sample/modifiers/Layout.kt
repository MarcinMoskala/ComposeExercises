@file:Suppress("unused", "UNUSED_VARIABLE", "UNUSED_PARAMETER")

package com.marcinmoskala.composeexercises.sample.modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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