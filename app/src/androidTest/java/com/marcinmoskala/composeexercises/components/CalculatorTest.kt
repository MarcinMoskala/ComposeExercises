package com.marcinmoskala.composeexercises.components

import androidx.compose.ui.test.junit4.createComposeRule
import com.marcinmoskala.composeexercises.sample.sample.Calculator
import org.junit.Rule
import org.junit.Test

class CalculatorTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    // While implementementing this test, you should find a mistake in calculator logic
    @Test
    fun basicUsage() {
        composeTestRule.setContent {
            Calculator()
        }

        // when I click 1, +, 2

        // the expression is "1 + 2", and the result is 3

        // when I click =

        // the expression is 3, and the result is 3

        // when I click *, 5, ., 5

        // the expression is "3 * 5.5", and the result is 16.5
    }

    // This tests should check functionality that is not yet implemented, implement it
    @Test
    fun dividingByZero() {
        composeTestRule.setContent {
            Calculator()
        }

        // when I click 1, /, 0

        // the expression is "1 / 0", and the result is "Infinity"
    }
}