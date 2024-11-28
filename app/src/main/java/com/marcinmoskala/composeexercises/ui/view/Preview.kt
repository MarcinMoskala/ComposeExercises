package com.marcinmoskala.composeexercises.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@Preview
@Composable
fun CoursesListScreen(
    @PreviewParameter(CoursesListWindowProvider::class)
    uiState: CoursesListState,
) {
    when {
        uiState.isLoading -> LoadingBar()
        uiState.list.isEmpty() -> EmptyScreen()
        else -> CoursesList(uiState.list)
    }
}

data class CoursesListState(
    val isLoading: Boolean = false,
    val list: List<Course> = emptyList(),
)

class CoursesListWindowProvider : PreviewParameterProvider<CoursesListState> {
    override val values: Sequence<CoursesListState>
        get() = sequenceOf(
            CoursesListState(isLoading = true, list = emptyList()),
            CoursesListState(isLoading = false, list = fakeCourses),
            CoursesListState(isLoading = false, list = emptyList()),
        )
}





