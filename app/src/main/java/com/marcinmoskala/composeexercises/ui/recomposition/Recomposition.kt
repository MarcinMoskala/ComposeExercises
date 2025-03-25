package com.marcinmoskala.composeexercises.ui.recomposition

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun Parent() {
    var counter by remember { mutableIntStateOf(0) }
    SideEffect { println("Parent recompose") }
    Child(counter = counter, onIncrement = { counter++ })
}

@Composable
private fun Child(counter: Int, onIncrement: () -> Unit) {
    SideEffect { println("Child recompose") }
    Button(onClick = onIncrement) {
        SideEffect { println("Button recompose") }
        Text("Click me: ${counter}")
    }
}

//@Preview
//@Composable
//private fun Parent() {
//    var counter by remember { mutableIntStateOf(0) }
//    SideEffect { println("Parent recompose") }
//    Child(counter = {counter}, onIncrement = { counter++ })
//}
//
//@Composable
//private fun Child(counter: () -> Int, onIncrement: () -> Unit) {
//    SideEffect { println("Child recompose") }
//    Button(onClick = onIncrement) {
//        Text("Click me: ${counter()}")
//    }
//}

// ***

@Preview
@Composable
private fun Recomposition() {
    var value by remember { mutableStateOf(false) }
    SideEffect { println("Recompose Recomposition") }
    Button(onClick = { value = !value }) {
        Text("Click me")
    }

    BodyReading(value)
    BodyWriting { value = it }
}

@Composable
private fun BodyReading(value: Boolean) {
    SideEffect { println("Recompose BodyReading") }
    SideEffect { println("Value: $value") }
    Text(" ")
    Text(" ")
}

@Composable
private fun BodyWriting(setValue: (Boolean) -> Unit) {
    SideEffect { println("Recompose BodyNotReading") }
    setValue(false)
    Text(" ")
    Text(" ")
}
//@Preview
//@Composable
//private fun Recomposition() {
//    val value = remember { mutableStateOf(false) }
//    SideEffect { println("Recompose Recomposition") }
//    Button(onClick = { value.value = !value.value }) {
//        Text("Click me")
//    }
//
//    BodyReading(value)
//    BodyWriting(value)
//}
//
//@Composable
//private fun BodyReading(value: MutableState<Boolean>) {
//    SideEffect { println("Recompose BodyReading") }
//    SideEffect { println("Value: ${value.value}") }
//    Text(" ")
//    Text(" ")
//}
//
//@Composable
//private fun BodyWriting(value: MutableState<Boolean>) {
//    SideEffect { println("Recompose BodyNotReading") }
//    value.value = false
//    Text(" ")
//    Text(" ")
//}