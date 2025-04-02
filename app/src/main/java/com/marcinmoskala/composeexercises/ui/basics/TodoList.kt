package com.marcinmoskala.composeexercises.ui.basics

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marcinmoskala.composeexercises.R
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Preview
@Composable
private fun TodoListScreen() {
    var todoItems by remember { mutableStateOf(persistentListOf<TodoItem>()) }
    var showAddItemDialog by remember { mutableStateOf(false) }
    if (showAddItemDialog) {
        AddItemDialog(
            onAddItem = { text ->
                todoItems = todoItems.add(TodoItem(text, done = false))
                showAddItemDialog = false
            },
            onDismiss = { showAddItemDialog = false }
        )
    }
    TodoListScreen(
        items = todoItems,
        onItemClicked = { item ->
            todoItems = todoItems.map {
                if (it == item) it.copy(done = !it.done) else it
            }.toPersistentList()
        },
        onAddNewItem = {
            showAddItemDialog = true
        }
    )
}

private data class TodoItem(val text: String, val done: Boolean)

@Composable
private fun TodoListScreen(
    items: PersistentList<TodoItem>,
    onItemClicked: (TodoItem) -> Unit,
    onAddNewItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNewItem) {
                Image(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new item"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding)
        ) {
            items(items) { item ->
                TodoItemRow(item, onItemClicked)
            }
        }
    }
}

@Composable
private fun TodoItemRow(item: TodoItem, onItemClicked: (TodoItem) -> Unit, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClicked(item) }
    ) {
        Image(
            painter = painterResource(id = if (item.done) R.drawable.check else R.drawable.uncheck),
            contentDescription = "Todo item icon",
            modifier = Modifier.size(40.dp)
                .padding(8.dp)
        )
        Text(
            text = item.text,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}

@Composable
private fun AddItemDialog(
    onAddItem: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add New Item") },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Item text") }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onAddItem(text)
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview
@Composable
private fun TodoListScreenPreview() {
    val todoItems = persistentListOf(
        TodoItem("Buy milk", done = false),
        TodoItem("Walk the dog", done = true),
        TodoItem("Do homework", done = false)
    )
    TodoListScreen(todoItems, {}, {})
}

@Preview
@Composable
private fun AddItemDialogPreview() {
    AddItemDialog({}, {})
}