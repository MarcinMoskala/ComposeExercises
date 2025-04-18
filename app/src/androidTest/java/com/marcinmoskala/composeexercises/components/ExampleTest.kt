package com.marcinmoskala.composeexercises.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.marcinmoskala.composeexercises.sample.sample.UserListPreview
import org.junit.Rule
import org.junit.Test

class ExampleTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Composable
    fun MyButton() {
        Button(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null, // Invisible in semantic tree, because no contentDescription
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing)) // Invisible in semantic tree, because no properties
            Text("Like")
        }
    }

    @Test
    fun showExampleTree1() {
        composeTestRule.setContent {
            UserListPreview()
        }

        composeTestRule.onRoot().printToLog("UserListPreview")
    }

    @Test
    fun showExampleTree2() {
        composeTestRule.setContent {
            MyButton()
        }

        composeTestRule.onRoot().printToLog("MyButton")
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("MyButton unmerged")
    }

    @Composable
    fun MyButtonChildless() {
        Button(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null, // Invisible in semantic tree, because no contentDescription
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing)) // Invisible in semantic tree, because no properties
            Text("Like")
        }
    }

    fun withRole(role: Role) = SemanticsMatcher.expectValue(SemanticsProperties.Role, role)

    @Test
    fun showMergedTree() {
        composeTestRule.setContent {
            MyButtonChildless()
        }

        // Pass
        composeTestRule.onNode(withRole(Role.Button), useUnmergedTree = true).onChild().assertTextEquals("Like")
        composeTestRule.onNode(withRole(Role.Button)).assertTextEquals("Like")
        // Fails
//        composeTestRule.onNode(withRole(Role.Button), useUnmergedTree = true).assertTextEquals("Like")
//        composeTestRule.onNode(withRole(Role.Button)).onChild().assertTextEquals("Like")
    }
}