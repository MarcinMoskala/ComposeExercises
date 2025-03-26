package com.marcinmoskala.composeexercises.ui.interop

import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ToastGreeting(greeting: String) {
    val context = LocalContext.current
    Button(onClick = {
        Toast.makeText(context, greeting, Toast.LENGTH_SHORT).show()
    }) {
        Text("Greet")
    }
}

@Preview
@Composable
fun PreviewToastGreeting() {
    ToastGreeting("Hello")
}