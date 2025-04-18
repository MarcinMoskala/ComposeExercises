package com.marcinmoskala.composeexercises.sample.sample

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
            contentDescription = "Picture of ${userRowUi.name}",
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
            contentDescription = "Picture of ${userRowUi.name}",
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

@Composable
fun UserList(
    users: List<UserRowUi>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(users, key = { it.name }) { user ->
            UserRow(user)
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

@Preview
@Composable
fun UserListPreview() {
    UserList(
        users = listOf(
            UserRowUi("Samuel Vimes", "Commander of the City Watch") {},
            UserRowUi("Carrot Ironfoundersson", "Watchman") {},
            UserRowUi("Angua von Überwald", "Watchwoman") {},
            UserRowUi("Nobby Nobbs", "Watchman") {},
            UserRowUi("Fred Colon", "Watchman") {},
            UserRowUi("Cheery Littlebottom", "Watchwoman") {},
            UserRowUi("Detritus", "Watchman") {},
            UserRowUi("Cuddy", "Watchman") {},
        )
    )
}