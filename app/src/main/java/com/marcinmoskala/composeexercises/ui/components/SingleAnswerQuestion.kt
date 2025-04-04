package com.marcinmoskala.composeexercises.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SingleAnswerQuestion(
    question: String,
    answers: List<String>,
    modifier: Modifier = Modifier,
    initialSelectedAnswer: String? = null,
    onAnswerSelected: (String) -> Unit = {},
) {
    var selectedAnswer by remember { mutableStateOf<String?>(initialSelectedAnswer) }
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = question,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        answers.forEach { answer ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedAnswer = answer
                        onAnswerSelected(answer)
                    }
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = answer == selectedAnswer,
                    onClick = {
                        selectedAnswer = answer
                        onAnswerSelected(answer)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = answer, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SingleAnswerQuestionPreview() {
    MaterialTheme {
        SingleAnswerQuestion(
            question = "What is your favorite fruit?",
            answers = listOf("Apple", "Banana", "Orange", "Strawberry"),
            onAnswerSelected = { answer ->
                println("Selected answer: $answer")
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SingleAnswerQuestionChosenAnswerPreview() {
    MaterialTheme {
        SingleAnswerQuestion(
            question = "What is your favorite fruit?",
            answers = listOf("Apple", "Banana", "Orange", "Strawberry"),
            onAnswerSelected = { answer ->
                println("Selected answer: $answer")
            },
            initialSelectedAnswer = "Orange"
        )
    }
}

//@Composable
//fun SingleAnswerQuestion(
//    question: String,
//    answers: List<String>,
//    selectedAnswer: String?,
//    onAnswerSelected: (String) -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    Column(modifier = modifier.padding(16.dp)) {
//        Text(
//            text = question,
//            style = MaterialTheme.typography.headlineMedium,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//        answers.forEach { answer ->
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable { onAnswerSelected(answer) }
//                    .padding(8.dp)
//            ) {
//                RadioButton(
//                    selected = answer == selectedAnswer,
//                    onClick = null
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(text = answer, style = MaterialTheme.typography.bodyMedium)
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun SingleAnswerQuestionPreview() {
//    MaterialTheme {
//        var selectedAnswer by remember { mutableStateOf<String?>(null) }
//        SingleAnswerQuestion(
//            question = "What is your favorite fruit?",
//            answers = listOf("Apple", "Banana", "Orange", "Strawberry"),
//            selectedAnswer = selectedAnswer,
//            onAnswerSelected = { answer ->
//                selectedAnswer = answer
//            }
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun SingleAnswerQuestionChosenAnswerPreview() {
//    MaterialTheme {
//        var selectedAnswer by remember { mutableStateOf("Orange") }
//        SingleAnswerQuestion(
//            question = "What is your favorite fruit?",
//            answers = listOf("Apple", "Banana", "Orange", "Strawberry"),
//            selectedAnswer = selectedAnswer,
//            onAnswerSelected = { answer ->
//                selectedAnswer = answer
//            }
//        )
//    }
//}