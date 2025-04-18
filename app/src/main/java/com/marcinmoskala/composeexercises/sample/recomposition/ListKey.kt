package com.marcinmoskala.composeexercises.sample.recomposition

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Preview
@Composable
private fun KeyExampleScreen() {
    var elements by remember { mutableStateOf(List(10) { "Item $it" }) }
    var counter by remember { mutableStateOf(0) }
    Scaffold(
        floatingActionButton = {
            AddActionButton(onClick = {
                elements = listOf("New item ${counter++}") + elements
            })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(elements) { elem ->
                Element(elem, onClick = { elements -= elem })
            }
        }
    }
}

@Composable
private fun Element(text: String, onClick: () -> Unit) {
    var loading by remember { mutableStateOf(true) }
    LaunchedEffect(text) {
        loading = true
        delay(1000) // Simulate loading
        loading = false
    }
    if (loading) {
        LoadingBar()
    } else {
        TextItem(text, onClick)
    }
}

@Composable
private fun AddActionButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        content = {
            Icon(Icons.Filled.Add, "Floating action button.")
        }
    )
}

@Composable
private fun TextItem(text: String, onClick: () -> Unit) {
    Text(
        text, fontSize = 32.sp, modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() })
}

@Composable
private fun LoadingBar() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}