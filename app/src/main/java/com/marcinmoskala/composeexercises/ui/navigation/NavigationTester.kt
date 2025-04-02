package com.marcinmoskala.composeexercises.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marcinmoskala.composeexercises.ui.navigation.exercise.ButtonConfig

@Preview
@Composable
@SuppressLint("RestrictedApi")
fun NavigationTester() {
    val navController = rememberNavController()
    fun backStackAsText(): String =
        navController.currentBackStack.value.mapNotNull { it.destination.route }.joinToString("\n")

    NavHost(navController, startDestination = "A") {
        composable("A") {
            GenericScreen(
                title = "Screen A",
                text = backStackAsText(),
                buttons = listOf(
                    ButtonConfig("Go to B") {
                        navController.navigate("B")
                    }
                )
            )
        }
        composable("B") {
            val counter = rememberSaveable { mutableStateOf(0) }
            GenericScreen(
                title = "Screen B (${counter.value})",
                text = backStackAsText(),
                buttons = listOf(
                    ButtonConfig("Go to C") {
                        counter.value++
                        navController.navigate("C")
                    }
                )
            )
        }
        composable("C") {
            GenericScreen(
                title = "Screen C",
                text = backStackAsText(),
                buttons = listOf(
                    ButtonConfig("Go to B") {
                        navController.navigate("B")
                    },
                    ButtonConfig("Go to A") {
                        navController.navigate("A")
                    }
                )
            )
        }
        composable("D") {
            GenericScreen(
                title = "Screen D",
                text = backStackAsText(),
                buttons = listOf(
                    ButtonConfig("Go to E") {
                        navController.navigate("E")
                    }
                )
            )
        }
        composable("E") {
            GenericScreen(
                title = "Screen F",
                text = backStackAsText(),
                buttons = listOf(
                    ButtonConfig("Go to F") {
                        navController.navigate("F")
                    },
                    ButtonConfig("Go to F restore") {
                        navController.navigate("F") {
                            restoreState = true
                        }
                    }
                )
            )
        }
        composable("F") {
            GenericScreen(
                title = "Screen F",
                text = backStackAsText(),
                buttons = listOf(
                    ButtonConfig("Go to A") {
                        navController.navigate("A")
                    }
                )
            )
        }
    }
}

@Composable
fun GenericScreen(
    title: String,
    text: String,
    buttons: List<ButtonConfig> = emptyList(),
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(text = title, fontSize = 24.sp, modifier = Modifier.padding(16.dp))
            buttons.forEach { button ->
                Button(
                    onClick = button.onClick,
                    modifier = Modifier.clickable { button.onClick() }
                ) {
                    Text(text = button.text)
                }
            }
            Text(text = text, modifier = Modifier.padding(16.dp))
        }
    }
}