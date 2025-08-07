package com.marcinmoskala.composeexercises.sample.recomposition

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlin.random.Random

// How Navigation helps with identity problem

@Preview
@Composable
private fun FlashcardApplication() {
    var screen by remember { mutableStateOf<FlashcardScreen>(FlashcardScreen.FlashcardList) }
    var flashcards by rememberSaveable { mutableStateOf(emptyList<Flashcard>()) }
    when (val s = screen) {
        is FlashcardScreen.AddFlashcard -> AddFlashcardScreen(
            addFlashcard = { question, answer ->
                flashcards += Flashcard(question, answer)
            },
            navigateToAddFlashcard = { screen = FlashcardScreen.AddFlashcard },
            navigateToFlashcardList = { screen = FlashcardScreen.FlashcardList },
        )

        is FlashcardScreen.FlashcardList -> FlashcardListScreen(
            flashcards = flashcards,
            navigateToAddFlashcard = { screen = FlashcardScreen.AddFlashcard }
        )
    }

//    val flashcards = rememberSaveable { mutableStateOf(emptyList<Flashcard>()) }
//    val navController = rememberNavController()
//    NavHost(navController, startDestination = "flashcardList") {
//        composable("flashcardList") {
//            FlashcardListScreen(
//                flashcards = flashcards.value,
//                navigateToAddFlashcard = { navController.navigate("addFlashcard") }
//            )
//        }
//        composable("addFlashcard") {
//            AddFlashcardScreen(
//                addFlashcard = { question, answer ->
//                    flashcards.value += Flashcard(question, answer)
//                },
//                navigateToAddFlashcard = { navController.navigate("addFlashcard") },
//                navigateToFlashcardList = { navController.navigate("flashcardList") }
//            )
//        }
//    }
}

sealed class FlashcardScreen {
    data object FlashcardList : FlashcardScreen()
    data object AddFlashcard : FlashcardScreen()
//    data class AddFlashcard(val number: Int) : FlashcardScreen()
}

private data class Flashcard(
    val question: String,
    val answer: String,
)

@Composable
private fun FlashcardListScreen(
    navigateToAddFlashcard: () -> Unit,
    flashcards: List<Flashcard>,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToAddFlashcard) {
                Icon(Icons.Default.Add, contentDescription = "Add flashcard")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            items(flashcards) {
                Text(it.question)
            }
        }
    }
}

@Composable
private fun AddFlashcardScreen(
    addFlashcard: (String, String) -> Unit,
    navigateToAddFlashcard: () -> Unit,
    navigateToFlashcardList: () -> Unit,
) {
    var question by rememberSaveable { mutableStateOf("") }
    var answer by rememberSaveable { mutableStateOf("") }
    var isAdding by rememberSaveable { mutableStateOf(true) }
    Column {
        TextField(
            enabled = isAdding,
            value = question,
            onValueChange = { question = it },
            label = { Text("Question") }
        )
        TextField(
            enabled = isAdding,
            value = answer,
            onValueChange = { answer = it },
            label = { Text("Answer") }
        )
        if (isAdding) {
            Button(onClick = {
                addFlashcard(question, answer)
                isAdding = false
            }) {
                Text("Add flashcard")
            }
        } else {
            Text("Flashcard added!")
            Button(onClick = {
                navigateToAddFlashcard()
            }) {
                Text("Add another flashcard")
            }
            Button(onClick = {
                navigateToFlashcardList()
            }) {
                Text("Back to list")
            }
        }
    }
}

// Identity problem

@Preview
@Composable
private fun GuessNumberGame() {
    var correctNumber: Int by remember { mutableIntStateOf(0) }
    var answerCorrect: Boolean? by remember { mutableStateOf(null) }
    Column {
//        if (answerCorrect == null) {
        NumberInput(
            enabled = answerCorrect == null,
            onChoose = { answerCorrect = it == correctNumber }
        )
//        }
        Text(
            text = when (answerCorrect) {
                true -> "Correct!"
                false -> "Incorrect!"
                null -> "Guess the number"
            }
        )
        if (answerCorrect != null) {
            Button(onClick = {
                correctNumber = randomNumber()
                answerCorrect = null
            }) {
                Text("Next number")
            }
        }
    }
}

@Composable
private fun NumberInput(enabled: Boolean, onChoose: (Int) -> Unit) {
    var field by remember { mutableStateOf("") }
    Box {
        TextField(
            enabled = enabled,
            value = field,
            onValueChange = {
                field = it.filter { it.isDigit() }
                it.toIntOrNull()?.let { onChoose(it) }
            },
            label = { Text("Number") }
        )
    }
}

private fun randomNumber() = (1..5).random()

// ***

@Preview
@Composable
private fun A() {
    var a by remember { mutableStateOf(Random.nextBoolean()) }
    Column {
        DisplayRandomValue()
        HorizontalDivider()

        DisplayRandomValue(value = a)

        HorizontalDivider()

        if (a) {
            DisplayRandomValue(value = true)
        } else {
            DisplayRandomValue(value = false)
        }

        HorizontalDivider()
        if (a) {
            DisplayRandomValue()
        }
        HorizontalDivider()
        DisplayRandomValue()
        Text("Change", modifier = Modifier.clickable { a = !a })
    }
}

@Composable
private fun DisplayRandomValue(value: Boolean = true) {
    val s = rememberSaveable { Random.nextInt().toString() }
    Text("$value, $s")
}

// *** movableContentOf

@Preview
@Composable
private fun ExampleMovableContent() {
    val content: @Composable () -> Unit = remember {
        {
            DisplayRandomValue()
            DisplayRandomValue()
            DisplayRandomValue()
        }
    }
    var horizontal by remember { mutableStateOf(false) }

    Column {
        if (horizontal) {
            Row {
                content()
            }
        } else {
            Column {
                content()
            }
        }
        Button(onClick = { horizontal = true }) {
            Text("Horizontal")
        }
        Button(onClick = { horizontal = false }) {
            Text("Vertical")
        }
    }
}
