package com.marcinmoskala.composeexercises.ui.recomposition

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

//@Immutable
//@Stable
//class ImmutableList<T>(val list: List<T>): List<T> by list

@Composable
@Preview
fun Stability() {
    val counter = remember { mutableIntStateOf(0) }
    val list = remember { List(10) { "Item $it" } }

    Column {
        Text("Counter: ${counter.intValue}")
        Button(onClick = { counter.intValue++ }) {
            Text("Increment")
        }
        Children(list)
    }
}

@Composable
fun Children(list: List<String>) {
    println("Recompose Children")
    Column {
        list.forEach {
            println("Recompose $it")
            Text(it)
        }
    }
}