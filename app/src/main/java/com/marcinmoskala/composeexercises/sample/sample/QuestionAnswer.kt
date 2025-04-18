//package com.marcinmoskala.composeexercises.ui.sample
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.marcinmoskala.composeexercises.ui.recomposition.UiState
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//
//class ExerciseViewModel {
//    private val _uiState = MutableStateFlow(UiState(
//        questionAnswer = 0,
//        questionOptions = listOf("Option 1", "Option 2", "Option 3"),
//        onAnswerSelected = ::onAnswerSelected,
//    ))
//    val uiState = _uiState.asStateFlow()
//
//    private fun onAnswerSelected(answer: Int) {
//        _uiState.update { it.copy(questionAnswer = answer) }
//    }
//}
//
//data class UiState(
//    val questionAnswer: Int,
//    val questionOptions: List<String>,
//    val onAnswerSelected: (Int) -> Unit,
//)
//
//@Composable
//fun ExerciseScreen(
//    vm: ExerciseViewModel,
//) {
//    val uiState by vm.uiState.collectAsStateWithLifecycle()
//    ExerciseScreen(
//        uiState = uiState,
//    )
//}
//
////class ExerciseViewModel {
////    private val _questionOptions = MutableStateFlow(listOf<String>())
////    val questionOptions = _questionOptions.asStateFlow()
////
////    private val _questionAnswer = MutableStateFlow(0)
////    val questionAnswer = _questionAnswer.asStateFlow()
////
////    fun onAnswerSelected(answer: Int) {
////        _questionAnswer.value = answer
////    }
////}
////
////@Composable
////fun ExerciseScreen(
////    vm: ExerciseViewModel,
////) {
////    val questionAnswer by vm.questionAnswer.collectAsStateWithLifecycle()
////    val questionOptions by vm.questionOptions.collectAsStateWithLifecycle()
////    ExerciseScreen(
////        questionAnswer = questionAnswer,
////        questionOptions = questionOptions,
////        onAnswerSelected = vm::onAnswerSelected,
////    )
////}
//
//@Composable
//fun ExerciseScreen(
//    questionAnswer: Int,
//    questionOptions: List<String>,
//    onAnswerSelected: (Int) -> Unit,
//) {
//    SingleChoiceQuestion(
//        answer = questionAnswer,
//        setAnswer = onAnswerSelected,
//    )
//}
//
//@Composable
//fun SingleChoiceQuestion(
//    answer: Int,
//    setAnswer: (Int) -> Unit,
//) {
//    // ...
//}