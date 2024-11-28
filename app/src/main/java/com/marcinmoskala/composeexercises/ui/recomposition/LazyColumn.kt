package com.marcinmoskala.composeexercises.ui.recomposition

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun LazyColumnExample() {
    LazyColumn {
        items(itemsListA, key = {it}) { item ->
            A(item)
        }
        stickyHeader {
            B()
        }
        item {
            AdView()
        }
        items(itemsListC, key = {it}) { item ->
            C(item)
        }
    }
}

val itemsListA = (0..20).map { "A$it" }
val itemsListC = (0..20).map { "C$it" }

@Composable
fun A(item: String) {
    Text("A: $item", modifier = Modifier.padding(16.dp))
}

@Composable
fun B() {
    Text("B", modifier = Modifier.padding(16.dp))
}

@Composable
fun C(item: String) {
    Text("C: $item", modifier = Modifier.padding(16.dp))
}

@Composable
fun AdView() {
    DisposableEffect(Unit) {
        println("AdView started")
        onDispose {
            println("AdView onDispose")
        }
    }
    Text("AdView", modifier = Modifier.padding(16.dp))
}