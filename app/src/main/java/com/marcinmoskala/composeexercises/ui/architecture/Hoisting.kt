package com.marcinmoskala.composeexercises.ui.architecture

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun SingleChoiceQuestion(
    question: String,
    options: List<String>,
    selectedOption: String?,
    onOptionClicked: (String) -> Unit,
) {
    // ...
}

@Composable
fun SingleChoiceQuestion(
    question: String,
    options: List<String>,
    onSelectedChanged: (String) -> Unit,
) {
    var selected: String? by remember { mutableStateOf(null) }
    // ...
}