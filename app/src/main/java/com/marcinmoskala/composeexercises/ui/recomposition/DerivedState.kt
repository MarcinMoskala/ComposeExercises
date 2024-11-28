package com.marcinmoskala.composeexercises.ui.recomposition

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun DerivedState() {

    var state by remember { mutableStateOf("A") }
    var otherState by remember { mutableStateOf("A") }

    val calculatedState = calculate("calculatedState", state)
    val recalculatedState = remember(state) { calculate("recalculatedState", state) }
    val derivedState by remember { derivedStateOf { calculate("derivedState", state) } }

    Column {
        Button(onClick = { state += "A" }) {
            Text("Change state")
        }
        Button(onClick = { otherState += "A" }) {
            Text("Change otherState")
        }
        Text("State: $state")
//        Text("Derived state: $derivedState")
//        Text("Calculated state: $calculatedState")
//        Text("Recalculated state: $recalculatedState")
//        Text("Other state: $otherState")
    }
}

fun calculate(name: String, value: String): String {
    println("Calculating $name")
    return value.lowercase()
}

// ***

@Preview
@Composable
fun ThresholdExample() {
    val threshold = remember { mutableStateOf(0) }

    Column {
        CreateUsernameCalculated(threshold.value)
        CreateUsernameRemembered(threshold.value)
        CreateUsernameDerived(threshold.value)

        Button(onClick = { threshold.value++ }) {
            Text("Increase threshold")
        }
        Button(onClick = { threshold.value-- }) {
            Text("Decrease threshold")
        }
    }
}

@Composable
fun CreateUsernameCalculated(threshold: Int) {
    val username = remember { mutableStateOf("User") }
    val enabled = username.value.length > threshold
    println("CreateUsernameCalculated recomposed")
    Column {
        println("CreateUsernameCalculated.Column recomposed")
        Text("CreateUsernameCalculated")
        UsernameTextField(username)
        CreateButton { enabled }
    }
}

@Composable
fun CreateUsernameRemembered(threshold: Int) {
    val username = remember { mutableStateOf("User") }
    val enabled = remember(username.value, threshold) { username.value.length > threshold }
    println("CreateUsernameRemembered recomposed")
    Column {
        println("CreateUsernameRemembered.Column recomposed")
        Text("CreateUsernameRemembered")
        UsernameTextField(username)
        CreateButton { enabled }
    }
}

@Composable
fun CreateUsernameDerived(threshold: Int) {
    val username = remember { mutableStateOf("User") }
    val enabled = remember(threshold) { derivedStateOf { username.value.length > threshold } }
    println("CreateUsernameDerived recomposed")
    Column {
        println("CreateUsernameDerived.Column recomposed")
        Text("CreateUsernameDerived")
        UsernameTextField(username)
        CreateButton { enabled.value }
    }
}

@Composable
fun UsernameTextField(username: MutableState<String>) {
    println("UsernameTextField recomposed")
    TextField(value = username.value, onValueChange = { username.value = it })
}

@Composable
fun CreateButton(enabled: () -> Boolean) {
    println("CreateButton recomposed")
    Button(enabled = enabled(), onClick = { /* Create user */ }) {
        Text("Create")
    }
}