package com.marcinmoskala.composeexercises.sample.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//@Composable
//fun SingleAnswerQuestion(
//    question: String,
//    answers: List<String>,
//    modifier: Modifier = Modifier,
//    initialSelectedAnswer: String? = null,
//    onAnswerSelected: (String) -> Unit = {},
//) {
//    var selectedAnswer by remember { mutableStateOf(initialSelectedAnswer) }
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
//                    .clickable {
//                        selectedAnswer = answer
//                        onAnswerSelected(answer)
//                    }
//                    .padding(8.dp)
//            ) {
//                RadioButton(
//                    selected = answer == selectedAnswer,
//                    onClick = {
//                        selectedAnswer = answer
//                        onAnswerSelected(answer)
//                    }
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
//        SingleAnswerQuestion(
//            question = "What is your favorite fruit?",
//            answers = listOf("Apple", "Banana", "Orange", "Strawberry"),
//            onAnswerSelected = { answer ->
//                println("Selected answer: $answer")
//            },
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun SingleAnswerQuestionChosenAnswerPreview() {
//    MaterialTheme {
//        SingleAnswerQuestion(
//            question = "What is your favorite fruit?",
//            answers = listOf("Apple", "Banana", "Orange", "Strawberry"),
//            onAnswerSelected = { answer ->
//                println("Selected answer: $answer")
//            },
//            initialSelectedAnswer = "Orange"
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun TestDifferentQuestions() {
//    data class QuestionSheet(
//        val question: String,
//        val answers: List<String>,
//        val selectedAnswer: String? = null
//    )
//    var questionSheets by remember {
//        mutableStateOf(
//            listOf(
//                QuestionSheet(
//                    question = "What is your favorite fruit?",
//                    answers = listOf("Apple", "Banana", "Orange", "Strawberry"),
//                    selectedAnswer = null,
//                ),
//                QuestionSheet(
//                    question = "What fruit do you hate most?",
//                    answers = listOf("Apple", "Banana", "Orange", "Strawberry"),
//                    selectedAnswer = null,
//                ),
//            )
//        )
//    }
//    var selectedQuestionNumber by remember { mutableStateOf(0) }
//    val questionSheet = questionSheets[selectedQuestionNumber]
//    MaterialTheme {
//        Column {
//            SingleAnswerQuestion(
//                question = questionSheet.question,
//                answers = questionSheet.answers,
//                onAnswerSelected = { answer ->
//                    questionSheets = questionSheets.mapIndexed { index, questionSheet ->
//                        if (index == selectedQuestionNumber) {
//                            questionSheet.copy(selectedAnswer = answer)
//                        } else {
//                            questionSheet
//                        }
//                    }
//                },
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Button({selectedQuestionNumber = (selectedQuestionNumber + 1) % questionSheets.size}) {
//                Text("Next question")
//            }
//        }
//    }
//}

@Composable
fun SingleAnswerQuestion(
    question: String,
    answers: List<String>,
    selectedAnswer: String?,
    onAnswerSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
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
                    .clickable { onAnswerSelected(answer) }
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = answer == selectedAnswer,
                    onClick = { onAnswerSelected(answer) },
                    modifier = Modifier.testTag("RadioButton")
                        .semantics { contentDescription = "RadioButton for answer $answer" }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = answer, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SingleAnswerQuestionPreview() {
    MaterialTheme {
        var selectedAnswer by remember { mutableStateOf<String?>(null) }
        SingleAnswerQuestion(
            question = "What is your favorite fruit?",
            answers = listOf("Apple", "Banana", "Orange", "Strawberry"),
            selectedAnswer = selectedAnswer,
            onAnswerSelected = { answer ->
                selectedAnswer = answer
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SingleAnswerQuestionChosenAnswerPreview() {
    MaterialTheme {
        var selectedAnswer by remember { mutableStateOf("Orange") }
        SingleAnswerQuestion(
            question = "What is your favorite fruit?",
            answers = listOf("Apple", "Banana", "Orange", "Strawberry"),
            selectedAnswer = selectedAnswer,
            onAnswerSelected = { answer ->
                selectedAnswer = answer
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TestDifferentQuestions() {
    data class QuestionSheet(
        val question: String,
        val answers: List<String>,
        val selectedAnswer: String? = null
    )
    var questionSheets by remember {
        mutableStateOf(
            listOf(
                QuestionSheet(
                    question = "What is your favorite fruit?",
                    answers = listOf("Apple", "Banana", "Orange", "Strawberry"),
                    selectedAnswer = null,
                ),
                QuestionSheet(
                    question = "What fruit do you hate most?",
                    answers = listOf("Apple", "Banana", "Orange", "Strawberry"),
                    selectedAnswer = null,
                ),
            )
        )
    }
    var selectedQuestionNumber by remember { mutableStateOf(0) }
    val questionSheet = questionSheets[selectedQuestionNumber]
    MaterialTheme {
        Column {
            SingleAnswerQuestion(
                question = questionSheet.question,
                answers = questionSheet.answers,
                selectedAnswer = questionSheet.selectedAnswer,
                onAnswerSelected = { answer ->
                    questionSheets = questionSheets.mapIndexed { index, questionSheet ->
                        if (index == selectedQuestionNumber) {
                            questionSheet.copy(selectedAnswer = answer)
                        } else {
                            questionSheet
                        }
                    }
                },
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button({selectedQuestionNumber = (selectedQuestionNumber + 1) % questionSheets.size}) {
                Text("Next question")
            }
        }
    }
}

// ***

fun VerticalScrollerState(): VerticalScrollerState = VerticalScrollerStateImpl()

interface VerticalScrollerState {
    var scrollPosition: Int
    var scrollRange: Int
}

private class VerticalScrollerStateImpl(
    scrollPosition: Int = 0,
    scrollRange: Int = 0
) : VerticalScrollerState {
    private var _scrollPosition by mutableStateOf(scrollPosition, structuralEqualityPolicy())
    override var scrollPosition: Int
        get() = _scrollPosition
        set(value) {
            _scrollPosition = value.coerceIn(0, scrollRange)
        }

    private var _scrollRange by mutableStateOf(scrollRange, structuralEqualityPolicy())
    override var scrollRange: Int
        get() = _scrollRange
        set(value) {
            require(value >= 0) { "$value must be > 0" }
            _scrollRange = value
            scrollPosition = scrollPosition
        }
}

@Composable
fun VerticalScroller(
    verticalScrollerState: VerticalScrollerState = remember { VerticalScrollerState() }
) {
    // ...
}