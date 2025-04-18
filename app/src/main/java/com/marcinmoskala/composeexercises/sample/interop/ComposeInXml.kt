package com.marcinmoskala.composeexercises.sample.interop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.ComposeView
import com.marcinmoskala.composeexercises.R

class ComposeInXmlActivity : ComponentActivity() {
    private val greeting = "from XML!"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose_in_xml)
        findViewById<ComposeView>(R.id.composeView).setContent {
            AnimatedGreeting(greeting)
        }
    }
}