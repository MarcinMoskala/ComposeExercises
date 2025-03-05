package com.marcinmoskala.composeexercises.ui.state

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

// Write some text on preview and rotate
@Preview
@Composable
private fun RememberSavable() {
    var email by remember { mutableStateOf("") }
    var emailSavable by rememberSaveable { mutableStateOf("") }

    Column {
        TextField(value = email, onValueChange = { email = it })
        TextField(value = emailSavable, onValueChange = { emailSavable = it })
    }
}