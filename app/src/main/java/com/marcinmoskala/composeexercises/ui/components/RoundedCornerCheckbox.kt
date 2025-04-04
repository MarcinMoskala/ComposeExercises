package com.marcinmoskala.composeexercises.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RoundedCornerCheckbox(
    isChecked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Float = 24f,
    checkedColor: Color = Color.Blue,
    uncheckedColor: Color = Color.White,
    shape: Shape = RoundedCornerShape(6.dp),
) {
    val checkboxColor: Color by animateColorAsState(if (isChecked) checkedColor else uncheckedColor)
    val density = LocalDensity.current
    val duration = 200

    Box(
        modifier = modifier
            .testTag("RoundedCornerCheckbox")
            .clickable { onClick() }
            .size(size.dp)
            .background(color = checkboxColor, shape = shape)
            .border(width = 1.5.dp, color = checkedColor, shape = shape),
        contentAlignment = Alignment.Center,

    ) {
        AnimatedVisibility(
            visible = isChecked,
            enter = slideInHorizontally(animationSpec = tween(duration)) {
                with(density) { (size * -0.5).dp.roundToPx() }
            } + expandHorizontally(
                expandFrom = Alignment.Start,
                animationSpec = tween(duration)
            ),
            exit = fadeOut()
        ) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Checked",
                tint = uncheckedColor
            )
        }
    }
}

@Preview
@Composable
fun RoundedCornerCheckboxPreview() {
    var checked by remember { mutableStateOf(true) }
    RoundedCornerCheckbox(
        isChecked = checked,
        onClick = { checked = !checked },
    )
}