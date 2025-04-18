package com.marcinmoskala.composeexercises.sample.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp


@Preview
@Composable
fun TextPreview() {
    Column {
        Text(
            text = "Hello",
            fontSize = 20.sp,
            color = Red
        )
        BasicText(
            text = "Hello",
            style = TextStyle(
                fontSize = 20.sp,
                color = Red
            )
        )
    }
}