package com.marcinmoskala.composeexercises.ui.basics

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

class ContactsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactsScreen()
        }
    }
}

private data class Contact(
    val fullName: String,
    val label: String,
)

@Composable
private fun ContactsScreen() {
    // TODO
}

@Composable
private fun ContactItem(contact: Contact) {
    // TODO
}

@Composable
private fun AddContactDialog(
    onAddItem: (Contact) -> Unit,
    onDismiss: () -> Unit
) {
    var fillName by remember { mutableStateOf("") }
    var label by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add New Item") },
        text = {
            Column {
                TextField(
                    value = fillName,
                    onValueChange = { fillName = it },
                    label = { Text("Full name") }
                )
                TextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text("Label") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onAddItem(Contact(fillName, label))
                    onDismiss()
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
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ContactItemPreview() {
    ContactItem(Contact("John Doe", "Friend"))
}

@Preview
@Composable
private fun AddContactDialogPreview() {
    AddContactDialog({}, {})
}