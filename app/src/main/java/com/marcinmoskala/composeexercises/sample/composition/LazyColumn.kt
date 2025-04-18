package com.marcinmoskala.composeexercises.sample.composition

import android.os.Parcelable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinx.parcelize.Parcelize

@Preview
@Composable
fun CoursesList(
    courses: List<Course> = items,
) {
    var coursesToShow by rememberSaveable { mutableStateOf(courses) }
    LazyColumn {
        items(coursesToShow, key = { it }) { course ->
            CourseItem(course, onClick = { coursesToShow -= course })
        }
    }

//    Column {
//        for (course in courses) {
//            CourseItem(course)
//        }
//    }
//
//    LazyColumn {
//        items(courses, key = { it.id }) { course ->
//            CourseItem(course)
//        }
//    }
}

@Composable
fun CourseItem(course: Course, onClick: (Course) -> Unit = {}) {
    SideEffect { println("Composing $course") }
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { onClick(course) }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = course.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp)
            )
            Column {
                Text(course.name, fontSize = 20.sp, modifier = Modifier.padding(8.dp))
                Text(course.description, fontSize = 12.sp, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Parcelize
data class Course(
    val id: String,
    val imageUrl: String,
    val name: String,
    val description: String,
) : Parcelable

val items = List(100) {
    Course(
        id = "course$it",
        imageUrl = "https://picsum.photos/2000/3000?random=$it",
        name = "Name $it",
        description = "Description $it",
    )
}