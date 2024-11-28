package com.marcinmoskala.composeexercises.ui.recomposition

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

// Odczyt powoduje rekompozycję!
@Preview
@Composable
fun Recomposition() {
    val value = remember { mutableStateOf(false) }
    println("Recompose Recomposition")
    Button(onClick = { value.value = !value.value }) {
        Text("Click me")
    }

    BodyReading(value)
    BodyWriting(value)
}

@Composable
fun BodyReading(value: MutableState<Boolean>) {
    println("Recompose BodyReading")
    println("Value: ${value.value}")
    Text(" ")
    Text(" ")
}

@Composable
fun BodyWriting(value: MutableState<Boolean>) {
    println("Recompose BodyNotReading")
    value.value = false
    Text(" ")
    Text(" ")
}