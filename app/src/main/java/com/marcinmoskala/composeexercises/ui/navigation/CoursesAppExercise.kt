package com.marcinmoskala.composeexercises.ui.navigation.exercise

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

sealed class Screen() {
    @Serializable
    data object LoginStart : Screen()
    @Serializable
    data object Login : Screen()
    @Serializable
    data object Register : Screen()
    @Serializable
    data object Home : Screen()
    @Serializable
    data object Courses : Screen()
    @Serializable
    data class Course(val courseId: String) : Screen()
    @Serializable
    data class Lesson(val courseId: String, val lessonId: String) : Screen()
    @Serializable
    data class Challenge(val courseId: String, val lessonId: String, val challengeId: String) : Screen()
    @Serializable
    data object Settings : Screen()
}



@Preview
@Composable
fun CoursesAppExercise() {
    val navController = rememberNavController()
    var isLoggedIn by remember { mutableStateOf(false) }
    NavHost(
        navController = navController,
        startDestination = Screen.Home,
    ) {
        // Auth navigation graph
        navigation<Screen.LoginStart>(startDestination = Screen.Login) {
            composable<Screen.Login> {
                LoginScreen(
                    onLoggedIn = {},
                    onNavigateToRegister = {},
                )
            }

            composable<Screen.Register> {
                RegisterScreen(
                    onRegistered = {},
                    onBackToLogin = {},
                )
            }
        }

        composable<Screen.Home> {
            if (!isLoggedIn) {
                // If not logged in, navigate to login
            }
            HomeScreen(
                onNavigateToCourse = { courseId -> },
                onNavigateToLesson = { courseId, lessonId -> },
                onNavigateToChallenge = { courseId, lessonId, challengeId -> },
                onBottomNavigateToCourses = { },
                onBottomNavigateToSettings = { },
            )
        }

        composable<Screen.Courses> {
            CoursesScreen(
                onNavigateToCourse = { courseId -> },
                onBottomNavigateToHome = { },
                onBottomNavigateToSettings = { },
            )
        }

        composable<Screen.Settings> {
            SettingsScreen(
                onBottomNavigateToHome = { },
                onBottomNavigateToCourses = { },
            )
        }

        // Course flow
        composable<Screen.Course> {
            val route = it.toRoute<Screen.Course>()
            CourseScreen(
                courseId = route.courseId,
                onNavigateToLesson = { courseId, lessonId -> },
                onBottomNavigateToHome = { },
                onBottomNavigateToSettings = { },
            )
        }

        composable<Screen.Lesson> {
            val route = it.toRoute<Screen.Lesson>()

            LessonScreen(
                courseId = route.courseId,
                lessonId = route.lessonId,
                onNavigateToChallenge = { courseId, lessonId, challengeId -> },
            )
        }

        composable<Screen.Challenge> {
            val route = it.toRoute<Screen.Challenge>()

            ChallengeScreen(
                courseId = route.courseId,
                lessonId = route.lessonId,
                challengeId = route.challengeId,
                onNavigateToHome = { },
                onNavigateToCourse = { courseId, lessonId -> },
                onNavigateToChallenge = { courseId, lessonId, challengeId -> }
            )
        }
    }
}

@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    onNavigateToRegister: () -> Unit,
) {
    GenericScreen(
        title = "Login",
        buttons = listOf(
            ButtonConfig("Login") { onLoggedIn() },
            ButtonConfig("Register") { onNavigateToRegister() }
        )
    )
}

@Composable
fun RegisterScreen(
    onRegistered: () -> Unit,
    onBackToLogin: () -> Unit,
) {
    GenericScreen(
        title = "Register",
        buttons = listOf(
            ButtonConfig("Register and go home") { onRegistered() },
            ButtonConfig("Back to login") { onBackToLogin() }
        )
    )
}

@Composable
fun HomeScreen(
    onNavigateToCourse: (String) -> Unit,
    onNavigateToLesson: (String, String) -> Unit,
    onNavigateToChallenge: (String, String, String) -> Unit,
    onBottomNavigateToCourses: () -> Unit,
    onBottomNavigateToSettings: () -> Unit,
) {
    GenericScreen(
        title = "Home",
        buttons = listOf(
            ButtonConfig("Make challenge") {
                onNavigateToChallenge(
                    "course234",
                    "lesson345",
                    "challenge123"
                )
            },
            ButtonConfig("Continue a course") { onNavigateToCourse("course234") },
            ButtonConfig("Continue a lesson") { onNavigateToLesson("course234", "lesson345") }
        ),
        bottomNavigation = listOf(
            ButtonConfig("Home") {},
            ButtonConfig("Courses") { onBottomNavigateToCourses() },
            ButtonConfig("Settings") { onBottomNavigateToSettings() },
        )
    )
}

@Composable
fun CoursesScreen(
    onNavigateToCourse: (String) -> Unit,
    onBottomNavigateToHome: () -> Unit,
    onBottomNavigateToSettings: () -> Unit,
) {
    GenericScreen(
        title = "Courses",
        buttons = listOf("course1", "course2", "course3").map { course ->
            ButtonConfig("Continue $course") { onNavigateToCourse(course) }
        },
        bottomNavigation = listOf(
            ButtonConfig("Home") { onBottomNavigateToHome() },
            ButtonConfig("Courses") { },
            ButtonConfig("Settings") { onBottomNavigateToSettings() },
        )
    )
}

@Composable
fun CourseScreen(
    courseId: String,
    onNavigateToLesson: (String, String) -> Unit,
    onBottomNavigateToHome: () -> Unit,
    onBottomNavigateToSettings: () -> Unit,
) {
    GenericScreen(
        title = "Course $courseId",
        buttons = listOf("lesson1", "lesson2", "lesson3").map { lesson ->
            ButtonConfig("Continue $lesson") { onNavigateToLesson(courseId, lesson) }
        },
        bottomNavigation = listOf(
            ButtonConfig("Home") { onBottomNavigateToHome() },
            ButtonConfig("Courses") { },
            ButtonConfig("Settings") { onBottomNavigateToSettings() },
        )
    )
}

@Composable
fun LessonScreen(
    courseId: String,
    lessonId: String,
    onNavigateToChallenge: (String, String, String) -> Unit,
) {
    GenericScreen(
        title = "Lesson $lessonId from $courseId",
        buttons = listOf("challenge1", "challenge2", "challenge3").map { challenge ->
            ButtonConfig("Continue $challenge") {
                onNavigateToChallenge(
                    courseId,
                    lessonId,
                    challenge
                )
            }
        },
    )
}

@Composable
fun ChallengeScreen(
    courseId: String,
    lessonId: String,
    challengeId: String,
    onNavigateToHome: () -> Unit,
    onNavigateToCourse: (String, String) -> Unit,
    onNavigateToChallenge: (String, String, String) -> Unit,
) {
    GenericScreen(
        title = "Challenge $challengeId from lesson $lessonId from course $courseId",
        buttons = listOfNotNull(
            if (challengeId != "challenge4") ButtonConfig("Next") {
                val nextChallengeId =
                    "challenge${challengeId.substringAfter("challenge").toInt() + 1}"
                onNavigateToChallenge(courseId, lessonId, nextChallengeId)
            } else null,
            ButtonConfig("Back to course") { onNavigateToCourse(courseId, lessonId) },
            ButtonConfig("Finish") { onNavigateToHome() },
        )
    )
}

@Composable
fun SettingsScreen(
    onBottomNavigateToHome: () -> Unit,
    onBottomNavigateToCourses: () -> Unit,
) {
    GenericScreen(
        title = "Settings",
        bottomNavigation = listOf(
            ButtonConfig("Home") { onBottomNavigateToHome() },
            ButtonConfig("Courses") { onBottomNavigateToCourses() },
            ButtonConfig("Settings") {},
        )
    )
}

@Composable
fun GenericScreen(
    title: String,
    buttons: List<ButtonConfig> = emptyList(),
    bottomNavigation: List<ButtonConfig>? = null,
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
        }
        bottomNavigation?.let {
            BottomAppBar(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    bottomNavigation.forEach { button ->
                        Button(
                            onClick = button.onClick,
                            modifier = Modifier.clickable { button.onClick() }
                        ) {
                            Text(text = button.text)
                        }
                    }
                }
            }
        }
    }
}

data class ButtonConfig(
    val text: String,
    val onClick: () -> Unit
)

@Preview
@Composable
fun GenericScreenPreview() {
    GenericScreen(
        title = "Title",
        buttons = listOf(
            ButtonConfig("Button 1") {},
            ButtonConfig("Button 2") {}
        ),
        bottomNavigation = listOf(
            ButtonConfig("Bottom 1") {},
            ButtonConfig("Bottom 2") {},
            ButtonConfig("Bottom 2") {},
        )
    )
}