package com.marcinmoskala.composeexercises.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.marcinmoskala.composeexercises.sample.components.RoundedCornerCheckbox
import org.junit.Rule
import org.junit.Test

class RoundedCornerCheckboxTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testRoundedCornerCheckbox() {
        var isChecked by mutableStateOf(false)
        composeTestRule.setContent {
            RoundedCornerCheckbox(
                isChecked = isChecked,
                onClick = { isChecked = !isChecked },
            )
        }

        // when
        composeTestRule.onNodeWithTag("RoundedCornerCheckbox").performClick()

        // then
        assert(isChecked) { "Checkbox should be checked" }
        composeTestRule.onNodeWithContentDescription("Checked").isDisplayed()

        // when
        composeTestRule.onNodeWithTag("RoundedCornerCheckbox").performClick()

        // then
        assert(!isChecked) { "Checkbox should be unchecked" }
        composeTestRule.onNodeWithContentDescription("Checked").isNotDisplayed()
    }
}