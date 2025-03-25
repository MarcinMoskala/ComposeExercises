@file:Suppress("unused")

package com.marcinmoskala.composeexercises.ui.recomposition

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.plus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// MutableList

@Composable
@Preview
fun MutableListSample() {
    val list by remember { mutableStateOf(mutableListOf<String>()) }

    Column {
        Button(onClick = { list.add("Item${list.size}") }) {
            Text("Add Item")
        }
        Column {
            list.forEach {
                Text(it)
            }
        }
    }
}

// PersistentList

@Composable
@Preview
fun PersistentListSample() {
    var list by remember { mutableStateOf(persistentListOf<String>()) }

    Column {
        Button(onClick = { list += "Item${list.size}" }) {
            Text("Add Item")
        }
        Column {
            ItemsColumn(list)
        }
    }
}

// MutableState

@Composable
@Preview
fun MutableStateSample() {
    val listState = remember { mutableStateOf(persistentListOf<String>()) }

    Column {
        Button(onClick = {
            listState.value += "Item${listState.value.size}"
        }) {
            Text("Add Item")
        }
        MutableStateChild(listState)
    }
}

@Composable
private fun MutableStateChild(
    listState: MutableState<PersistentList<String>>
) {
    Column {
        ItemsColumn(listState.value)
    }
}

// Lambda

@Composable
@Preview
fun MutableLambdaSample() {
    val listState = remember { mutableStateOf(persistentListOf<String>()) }

    Column {
        Button(onClick = {
            listState.value += "Item${listState.value.size}"
        }) {
            Text("Add Item")
        }
        MutableLambda(list = { listState.value })
    }
}

@Composable
private fun MutableLambda(
    list: () -> PersistentList<String>
) {
    Column {
        ItemsColumn(list())
    }
}

@Composable
private fun ItemsColumn(list: PersistentList<String>) {
    SideEffect { println("ItemsColumn recomposed with $list") }
    Column {
        list.forEach {
            Text(it)
        }
    }
}

// Stable and unstable classes

// Stable, because only val properties with stable types
class User(val name: String, val age: Int)
// Unstable, because var property
class UnstableUser1(val name: String, var age: Int)
// Unstable, because List is unstable
class UnstableUser2(val name: String, val tags: List<String>)
// Stability enforced by annotation
@Stable
class StableUser2(val name: String, val tags: List<String>)


/*
stable class User {
  stable val name: String
  stable val age: Int
  <runtime stability> = Stable
}
unstable class UnstableUser1 {
  stable val name: String
  stable var age: Int
  <runtime stability> = Unstable
}
unstable class UnstableUser2 {
  stable val name: String
  unstable val tags: List<String>
  <runtime stability> = Unstable
}
stable class StableUser2 {
  stable val name: String
  unstable val tags: List<String>
}
 */

// ImmutableList

//@Immutable
//class ImmutableList<T>(val list: List<T>): List<T> by list

private class Player(val name: String)

@Composable
@Preview
fun Stability() {
    val counter = remember { mutableIntStateOf(0) }
    val list = remember { List(10) { Player("Item $it") } }

    Column {
        println("Column recompose")
        Text("Counter: ${counter.intValue}")
        Button(onClick = { counter.intValue++ }) {
            Text("Increment")
        }
        Children(list.map { it.name })
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

// Correct mutable objects

class LoginViewModel {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()
}

@Composable
fun LoginScreen(
    viewModel: LoginViewModel, // Unstable, but ok if used correctly
) {
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    // ...
}

class LoginComponent {
    val loading = mutableStateOf(false)
}

@Composable
fun LoginScreen2(
    component: LoginComponent, // Stable
) {
    val loading by component.loading
    // ...
}

@Composable
fun LoginScreen3(
    loading: StateFlow<Boolean>, // Unstable, but ok if used correctly
) {
    val loading by loading.collectAsStateWithLifecycle()
    // ...
}