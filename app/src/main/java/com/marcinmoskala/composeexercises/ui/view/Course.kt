package com.marcinmoskala.composeexercises.ui.view

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun CoursesList(
    courses: List<Course> = fakeCourses,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.White),
    ) {
        items(courses, key = { it.id }) { course ->
            CourseItem(course)
        }
    }
}

@Composable
fun CourseItem(course: Course) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_menu_report_image),
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit,
                contentDescription = course.name,
                modifier = Modifier
                    .padding(12.dp)
                    .size(60.dp),
            )
            Column(
                modifier = Modifier.padding(12.dp),
            ) {
                Text(
                    course.name,
                    fontSize = 20.sp,
                )
                if (course.progress > 0) {
                    CustomLinearProgressIndicator(
                        progress = course.progress.toFloat() / 100,
                        modifier = Modifier
                            .width(150.dp)
                            .padding(top = 12.dp),
                    )
                }
            }
        }
    }
}

val Blue40 = Color(0xFF3A5BFF)
val Blue20 = Color(0xFFE0E6FF)

@Composable
fun CustomLinearProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    progressColor: Color = Blue40,
    backgroundColor: Color = Blue20,
    clipShape: Shape = RoundedCornerShape(16.dp),
) {
    Box(
        modifier = modifier
            .clip(clipShape)
            .background(backgroundColor)
            .height(8.dp)
    ) {
        Box(
            modifier = Modifier
                .background(progressColor)
                .fillMaxHeight()
                .fillMaxWidth(progress)
        )
    }
}

data class Course(
    val id: String,
    val name: String,
    val imageUrl: String,
    val progress: Int = 0,
)

val fakeCourses = listOf(
    Course(
        "1",
        "Course 1",
        "https://marcinmoskala.com/kt-academy-articles/promotion/developersextra_workshop_transparent_feb25.png",
        50
    ),
    Course(
        "2",
        "Course 2",
        "https://marcinmoskala.com/kt-academy-articles/promotion/developersextra_workshop_transparent_feb25.png"
    ),
    Course(
        "3",
        "Course 3",
        "https://marcinmoskala.com/kt-academy-articles/promotion/developersextra_workshop_transparent_feb25.png"
    ),
    Course(
        "4",
        "Course 4",
        "https://marcinmoskala.com/kt-academy-articles/promotion/developersextra_workshop_transparent_feb25.png"
    ),
    Course(
        "5",
        "Course 5",
        "https://marcinmoskala.com/kt-academy-articles/promotion/developersextra_workshop_transparent_feb25.png"
    ),
)