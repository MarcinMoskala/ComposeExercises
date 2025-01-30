package com.marcinmoskala.composeexercises.ui.animations

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marcinmoskala.composeexercises.ui.modifiers.noRippleClickable

@Preview
@Composable
private fun CustomAnimateContentSizeExample1() {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .background(Color.Blue)
            .animateContentSize()
            .height(if (expanded) 400.dp else 200.dp)
            .fillMaxWidth()
            .noRippleClickable { expanded = !expanded }
    )
}

@Preview
@Composable
private fun CustomAnimateContentSizeExample2() {
    var items by remember { mutableStateOf(listOf("Item 1", "Item 2", "Item 3")) }
    Column(
        modifier = Modifier
            .background(Color.Blue)
            .animateContentSize()
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val itemModifiers = Modifier
            .padding(8.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .background(Color.White)
            .fillMaxWidth()
            .padding(16.dp)
        items.forEach { item ->
            Text(
                text = item,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = itemModifiers
            )
        }
        Text(
            text = "Add item",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable { items += "Item ${items.size + 1}" }
                .then(itemModifiers)
        )
    }
}