package com.marcinmoskala.composeexercises.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marcinmoskala.composeexercises.R
import com.marcinmoskala.composeexercises.loremIpsum
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

//@Preview
//@Composable
//fun CoursesApp() {
//    val navController = rememberNavController()
//    NavHost(
//        navController = navController,
//        startDestination = "courses"
//    ) {
//        composable("courses") {
//            CoursesScreen(
//                onNavigateToCourse = { courseId ->
//                    navController.navigate("course/$courseId")
//                }
//            )
//        }
//        composable("course/{courseId}",
//            arguments = listOf(
//                navArgument("courseId") { type = NavType.StringType }
//            )
//        ) { backStackEntry ->
//            val arguments = requireNotNull(backStackEntry.arguments)
//            val courseId = requireNotNull(arguments.getString("courseId"))
//            CourseScreen(
//                courseId = courseId,
//                onNavigateToLesson = { lessonId ->
//                    navController.navigate("course/$courseId/lesson/$lessonId")
//                }
//            )
//        }
//        composable("course/{courseId}/lesson/{lessonId}",
//            arguments = listOf(
//                navArgument("courseId") { type = NavType.StringType },
//                navArgument("lessonId") { type = NavType.IntType }
//            ),
//            deepLinks = listOf(
//                navDeepLink { uriPattern = "https://kt.academy/lesson/{courseId}/{lessonId}" },
//            ),
//        ) { backStackEntry ->
//            val arguments = requireNotNull(backStackEntry.arguments)
//            val courseId = requireNotNull(arguments.getString("courseId"))
//            val lessonId = requireNotNull(arguments.getInt("lessonId"))
//            LessonScreen(courseId, lessonId,
//                onNext = { navController.navigate("course/$courseId/lesson/${lessonId + 1}") },
//                onPrev = { navController.navigate("course/$courseId/lesson/${lessonId - 1}") }
//            )
//        }
//    }
//}

sealed interface Screen {
    @Serializable
    data object Courses : Screen

    @Serializable
    data class Course(val courseId: String) : Screen

    @Serializable
    data class Lesson(val courseId: String, val lessonId: Int) : Screen
}

// Type-safe navigation
//@Preview
//@Composable
//fun CoursesApp() {
//    val navController = rememberNavController()
//    NavHost(
//        navController = navController,
//        startDestination = Screen.Courses
//    ) {
//        composable<Screen.Courses>() {
//            CoursesScreen(
//                onNavigateToCourse = { courseId ->
//                    navController.navigate(Screen.Course(courseId))
//                }
//            )
//        }
//        composable<Screen.Course> {
//            val route: Screen.Course = it.toRoute()
//            CourseScreen(courseId = route.courseId,
//                onNavigateToLesson = { lessonId ->
//                    navController.navigate(Screen.Lesson(route.courseId, lessonId))
//                }
//            )
//        }
//        composable<Screen.Lesson>(
////            deepLinks = listOf(
////                navDeepLink<Screen.Lesson>(basePath = "https://kt.academy/lesson")
////            )
//        ) {
//            val route: Screen.Lesson = it.toRoute()
//            LessonScreen(
//                courseId = route.courseId, lessonId = route.lessonId,
//                onNext = {
//                    navController.navigate(
//                        Screen.Lesson(
//                            route.courseId,
//                            route.lessonId + 1
//                        )
//                    )
//                },
//                onPrev = {
//                    navController.navigate(
//                        Screen.Lesson(
//                            route.courseId,
//                            route.lessonId - 1
//                        )
//                    )
//                },
//            )
//        }
//    }
//}

// Navigating back with Result
//@Preview
//@Composable
//private fun CoursesApp() {
//    val navController = rememberNavController()
//    NavHost(
//        navController = navController,
//        startDestination = Screen.Courses
//    ) {
//        composable<Screen.Courses>() {
//            CoursesScreen(
//                onNavigateToCourse = { courseId ->
//                    navController.navigate(Screen.Course(courseId))
//                }
//            )
//        }
//        composable<Screen.Course> {
//            val route: Screen.Course = it.toRoute()
//            val isSuccess = navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("isSuccess") ?: false
//            CourseScreen(
//                courseId = route.courseId,
//                onNavigateToLesson = { lessonId ->
//                    navController.navigate(Screen.Lesson(route.courseId, lessonId))
//                },
//                isSuccess = isSuccess
//            )
//        }
//        composable<Screen.Lesson> {
//            val route: Screen.Lesson = it.toRoute()
//            LessonScreen(
//                courseId = route.courseId, lessonId = route.lessonId,
//                onComplete = {
//                    navController.previousBackStackEntry?.savedStateHandle?.set("isSuccess", true)
//                    navController.popBackStack()
//                },
////                onNext = {
////                    navController.navigate(
////                        Screen.Lesson(
////                            route.courseId,
////                            route.lessonId + 1
////                        )
////                    )
////                },
////                onPrev = {
////                    navController.navigate(
////                        Screen.Lesson(
////                            route.courseId,
////                            route.lessonId - 1
////                        )
////                    )
////                },
//            )
//        }
//    }
//}

//inline fun <reified T> NavHostController.getFromResult(key: String): T? =
//    currentBackStackEntry?.savedStateHandle?.get<T>(key)
//
//fun NavHostController.popBackStackWithResult(builder: SavedStateHandle.() -> Unit) {
//    previousBackStackEntry?.savedStateHandle?.let(builder)
//    popBackStack()
//}

@Composable
private fun CoursesScreen(onNavigateToCourse: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(100) { index ->
            CourseItem(
                courseId = "course$index",
                courseName = "Course $index",
                onNavigateToCourse = onNavigateToCourse
            )
        }
    }
}

@Composable
private fun CourseItem(
    courseId: String,
    courseName: String,
    onNavigateToCourse: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onNavigateToCourse(courseId) }
            .background(Color.White)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.heart_full),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = courseName,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun CourseScreen(
    courseId: String,
    onNavigateToLesson: (Int) -> Unit,
    isSuccess: Boolean = true
) {
    var successVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        launch {
            if (isSuccess) {
                successVisible = true
                delay(1000)
                successVisible = false
            }
        }
    }
    Box {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
        ) {
            items(100) { index ->
                LessonItem(
                    lessonId = index,
                    lessonName = "Lesson $index",
                    onNavigateToLesson = onNavigateToLesson
                )
            }
        }
        AnimatedVisibility(
            successVisible,
            enter = scaleIn(tween(1000)),
            exit = scaleOut(tween(1000)),
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(R.drawable.check),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

@Composable
private fun LessonItem(
    lessonId: Int,
    lessonName: String,
    onNavigateToLesson: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onNavigateToLesson(lessonId) }
            .background(Color.White)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.heart_full),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = lessonName,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun LessonScreen(
    courseId: String,
    lessonId: Int,
    onNext: (() -> Unit)? = null,
    onPrev: (() -> Unit)? = null,
    onComplete: (() -> Unit)? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Lesson $lessonId from $courseId",
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = loremIpsum(10000),
            )
        }
        if (onNext != null) {
            Button(
                onClick = onNext,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Text("Next")
            }
        }
        if (onComplete != null) {
            Button(
                onClick = onComplete,
                colors = buttonColors(containerColor = Color.Green, contentColor = Color.Black),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text("Complete")
            }
        }
        if (onPrev != null) {
            Button(
                onClick = onPrev,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text("Prev")
            }
        }
    }
}

@Preview
@Composable
private fun CoursesScreenPreview() {
    CoursesScreen(
        onNavigateToCourse = {}
    )
}

@Preview
@Composable
private fun CourseScreenPreview() {
    CourseScreen(
        courseId = "course123",
        onNavigateToLesson = {}
    )
}

@Preview
@Composable
private fun LessonScreenPreview() {
    LessonScreen(
        courseId = "course123",
        lessonId = 123,
        onNext = {},
        onPrev = {},
        onComplete = {}
    )
}