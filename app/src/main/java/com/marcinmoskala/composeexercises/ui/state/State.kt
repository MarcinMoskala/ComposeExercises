@file:Suppress("NOTHING_TO_INLINE")

package com.marcinmoskala.composeexercises.ui.state

import android.widget.EditText
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import kotlin.reflect.KProperty

@Preview
@Composable
fun StateExamples() {
    Column {

        val state1 = remember { mutableStateOf("") }
        TextField(value = state1.value, onValueChange = { state1.value = it })

        var state2 by remember { mutableStateOf("") }
        TextField(value = state2, onValueChange = { state2 = it })

        val (value3, setValue3) = remember { mutableStateOf("") }
        TextField(value = value3, onValueChange = setValue3)

    }
}