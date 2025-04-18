package com.marcinmoskala.composeexercises.sample.interop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme

class ComposeInActivityActivity : ComponentActivity() {
    private val greeting = "from Activity!"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { // In here, we can call composables!
            MaterialTheme {
                AnimatedGreeting(text = greeting)
            }
        }
    }
}