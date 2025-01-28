package com.marcinmoskala.composeexercises.ui.animations

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

private val enterTransition = slideInHorizontally(
    initialOffsetX = { fullWidth -> fullWidth },
    animationSpec = tween(durationMillis = 2000)
) + fadeIn(animationSpec = tween(durationMillis = 2000))

private val exitTransition = slideOutHorizontally(
    targetOffsetX = { fullWidth -> -fullWidth },
    animationSpec = tween(durationMillis = 2000)
) + fadeOut(animationSpec = tween(durationMillis = 2000))

@Preview
@Composable
private fun NavigationTransitions() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "page1") {
        composable(
            route = "page1",
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },
        ) {
            Page(
                background = Color.Red,
                text = "Page 1",
                onNext = { navController.navigate("page2") }
            )
        }
        composable(
            route = "page2",
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },
        ) {
            Page(
                background = Color.Green,
                text = "Page 2",
                onNext = { navController.navigate("page3") }
            )
        }
        composable(
            route = "page3",
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },
        ) {
            Page(
                background = Color.Blue,
                text = "Page 3",
                onNext = { navController.navigate("page1") }
            )
        }
    }
}

@Composable
private fun Page(
    background: Color,
    text: String,
    onNext: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Text(
            text,
            fontSize = 24.sp,
            color = Color.White,
        )
        Button(
            onClick = onNext,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth()
        ) {
            Text("Next")
        }
    }
}