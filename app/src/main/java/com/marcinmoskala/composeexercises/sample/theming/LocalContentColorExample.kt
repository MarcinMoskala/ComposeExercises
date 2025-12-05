package com.marcinmoskala.composeexercises.sample.theming

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview



@Preview
@Composable
private fun Screen() {
    Column {
        CompositionLocalProvider(LocalContentColor provides Color.Red) {
            ShowGreeting()
        }
        CompositionLocalProvider(LocalContentColor provides Color.Green) {
            ShowGreeting()
        }
        CompositionLocalProvider(LocalContentColor provides Color.Blue) {
            ShowGreeting()
        }
    }
}

@Preview
@Composable
private fun Screen2() {
    Column {
        CompositionLocalProvider(LocalTextStyle provides LocalTextStyle.current.copy(color = Color.Red)) {
            ShowGreeting()
        }
        CompositionLocalProvider(LocalTextStyle provides LocalTextStyle.current.copy(color = Color.Green)) {
            ShowGreeting()
        }
        CompositionLocalProvider(LocalTextStyle provides LocalTextStyle.current.copy(color = Color.Blue)) {
            ShowGreeting()
        }
    }
}

@Composable
private fun ShowGreeting() {
    Text(text = "ABCD")
}

