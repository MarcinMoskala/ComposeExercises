package com.marcinmoskala.composeexercises.ui.sample

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marcinmoskala.composeexercises.R

data class UserRowUi(
    val name: String,
    val role: String,
    val onUserClick: () -> Unit
)

@Composable
fun UserRowSimplified(userRowUi: UserRowUi) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Column{
            Text(userRowUi.name, fontSize = 20.sp)
            Text(userRowUi.role)
        }
    }
}

@Composable
fun UserRow(userRowUi: UserRowUi) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .clickable { userRowUi.onUserClick() }
    ) {
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .size(80.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(userRowUi.name, fontSize = 20.sp)
            Text(userRowUi.role)
        }
    }
}

@Preview
@Composable
fun UserRowSimplifiedPreview() {
    UserRowSimplified(UserRowUi("Samuel Vimes", "Commander of the City Watch") {})
}

@Preview
@Composable
fun UserRowPreview() {
    UserRow(UserRowUi("Samuel Vimes", "Commander of the City Watch") {})
}