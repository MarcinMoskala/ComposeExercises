package com.marcinmoskala.composeexercises.ui.recomposition

import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun ComponentHierarchy() {
    val counter = remember { mutableIntStateOf(0) }
    val message = remember { mutableStateOf("Initial Message") }

    println("ComponentHierarchy recomposition")
    Column(
        verticalArrangement = Center,
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
            .fillMaxHeight()
    ) {
        println("Column recomposition")
        Buttons(counter, message)
        CounterText(counter)
        MessageText(message.value)
    }
}

@Composable
fun Buttons(
    counter: MutableIntState,
    message: MutableState<String>,
) {
    println("Buttons recomposition")
    Button(onClick = { counter.value++ }) {
        println("Button (Counter) recomposition")
        Text("Increment Counter")
    }
    Button(onClick = { message.value = "Message Updated" }) {
        println("Button (Message) recomposition")
        Text("Update Message")
    }
}

@Composable
fun CounterText(counter: MutableIntState) {
    println("CounterText recomposition")
    Text("Counter: ${counter.value}")
    CounterSmallLabel(counter.value < 10)
}

@Composable
fun CounterSmallLabel(isSmall: Boolean) {
    println("CounterSmallLabel recomposition")
    Text(if(isSmall) "small number" else "big number", fontSize = 10.sp)
}

@Composable
fun MessageText(message: String) {
    println("MessageText recomposition")
    Text("Message: $message")
}