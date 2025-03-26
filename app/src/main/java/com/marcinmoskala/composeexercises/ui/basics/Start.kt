package com.marcinmoskala.composeexercises.ui.basics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text

// 1. Show simple view
// 2. Extract content to a function
// 3. Add @Preview
// 4. Style it
// 5. Add mutable state, and button to change it
// 6. Add an animation
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Text("Hello world!")
        }
    }
}