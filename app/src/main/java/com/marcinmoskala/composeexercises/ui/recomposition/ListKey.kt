package com.marcinmoskala.composeexercises.ui.recomposition

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
fun KeyExampleScreen() {
    var elements by remember { mutableStateOf(List(10) { "Item $it" }) }

    Scaffold(
        floatingActionButton = {
            Text("Add", modifier = Modifier.clickable { elements = listOf("New item") + elements })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(elements, key = { it }) { elem ->
                Element(elem, onClick = { elements -= elem })
            }
        }
    }
}

@Composable
fun Element(text: String, onClick: () -> Unit) {
    var loading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
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
private fun TextItem(text: String, onClick: () -> Unit) {
    Text(
        text, fontSize = 32.sp, modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() })
}

@Composable
private fun LoadingBar() {
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}