package com.marcinmoskala.composeexercises.sample.animations

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marcinmoskala.composeexercises.sample.modifiers.noRippleClickable
import kotlinx.coroutines.delay

@Preview
@Composable
private fun AnimateNumber() {
    var number by remember { mutableIntStateOf(0) }
    val animatedNumber by animateIntAsState(targetValue = number)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = animatedNumber.toString(),
            fontSize = 30.sp,
            modifier = Modifier.padding(32.dp)
        )
        Button(onClick = {
            number += 100
        }) {
            Text("Increase number")
        }
    }
}

@Preview
@Composable
private fun AnimatedBox() {
    var isClicked by remember { mutableStateOf(false) }

    val textColor by animateColorAsState(if (isClicked) Color.Black else Color.White)
    val backgroundColor by animateColorAsState(if (isClicked) Color.White else Color.Black)
    val size by animateIntAsState(if (isClicked) 400 else 100)

    Box(
        modifier = Modifier
            .size(size.dp)
            .background(backgroundColor)
            .noRippleClickable { isClicked = !isClicked },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isClicked) "Clicked!" else "Click Me",
            color = textColor
        )
    }
}

private data class MySize(val width: Dp, val height: Dp)

@Preview
@Composable
private fun MyAnimation() {
    var targetSize by remember { mutableStateOf(MySize(0.dp, 0.dp)) }
    val animSize: MySize by animateValueAsState(
        targetSize,
        TwoWayConverter(
            convertToVector = { size: MySize ->
                AnimationVector2D(size.width.value, size.height.value)
            },
            convertFromVector = { vector: AnimationVector2D ->
                MySize(vector.v1.dp, vector.v2.dp)
            }
        ),
        label = "size"
    )
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        delay(1000)
        targetSize = MySize(400.dp, 400.dp)
    }
    Box(
        modifier = Modifier
            .size(animSize.width, animSize.height)
            .background(Color.Blue),
    )
}