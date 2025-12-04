package com.marcinmoskala.composeexercises.sample.theming

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.tooling.preview.Preview

data class Greeting(val text: String = "Hello, World!")

val LocalGreeting = compositionLocalOf { Greeting() }

@Composable
private fun Screen() {
    CompositionLocalProvider(LocalGreeting provides Greeting("Good morning!")) {
        A()
    }
}

@Preview
@Composable
private fun Screen2() {
    Column {
        A()
        CompositionLocalProvider(LocalGreeting provides Greeting("Good morning!")) {
            A()
        }
        CompositionLocalProvider(LocalGreeting provides Greeting("Goodbye!")) {
            A()
        }
    }
}

@Composable
private fun A() {
    B()
}

@Composable
private fun B() {
    C()
}

@Composable
private fun C() {
    val greeting = LocalGreeting.current
    Text(greeting.text)
}
