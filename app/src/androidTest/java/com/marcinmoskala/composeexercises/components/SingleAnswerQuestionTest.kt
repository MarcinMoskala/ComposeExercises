package com.marcinmoskala.composeexercises.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.isNotSelected
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.marcinmoskala.composeexercises.ui.components.SingleAnswerQuestion
import org.junit.Rule
import org.junit.Test

class SingleAnswerQuestionTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun answerSelected() {
        composeTestRule.setContent {
            SingleAnswerQuestion(
                question = "What is your favorite fruit?",
                answers = listOf("Apple", "Banana", "Orange", "Strawberry"),
                selectedAnswer = "Banana",
                onAnswerSelected = {}
            )
        }

        composeTestRule.onAllNodesWithTag("RadioButton")
            .filter(isSelected())
            .assertCountEquals(1)
    }

    @Test
    fun noAnswerSelected() {
        composeTestRule.setContent {
            SingleAnswerQuestion(
                question = "What is your favorite fruit?",
                answers = listOf("Apple", "Banana", "Orange", "Strawberry"),
                selectedAnswer = null,
                onAnswerSelected = {}
            )
        }

        composeTestRule.onAllNodesWithTag("RadioButton")
            .assertAll(isNotSelected())
    }

    @Test
    fun behaviorTest() {
        var selected by mutableStateOf<String?>(null)
        composeTestRule.setContent {
            SingleAnswerQuestion(
                question = "What is your favorite fruit?",
                answers = listOf("Apple", "Banana", "Orange", "Strawberry"),
                selectedAnswer = selected,
                onAnswerSelected = { selected = it }
            )
        }

        composeTestRule.onAllNodesWithTag("RadioButton")
            .assertAll(isNotSelected())

        composeTestRule.onNodeWithText("Apple").performClick()
        composeTestRule.onAllNodesWithTag("RadioButton")
            .filter(isSelected())
            .assertCountEquals(1)
        composeTestRule.onNodeWithContentDescription("RadioButton for answer Apple")
            .assertIsSelected()

        composeTestRule.onNodeWithContentDescription("RadioButton for answer Orange")
            .performClick()
        composeTestRule.onAllNodesWithTag("RadioButton")
            .filter(isSelected())
            .assertCountEquals(1)
        composeTestRule.onNodeWithContentDescription("RadioButton for answer Orange")
            .assertIsSelected()
    }
}