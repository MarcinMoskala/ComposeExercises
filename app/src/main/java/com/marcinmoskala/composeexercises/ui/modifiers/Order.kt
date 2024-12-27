package com.marcinmoskala.composeexercises.ui.modifiers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marcinmoskala.composeexercises.R

@Preview(device = Devices.PIXEL_4_XL)
@Composable
fun Order(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.size(100.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "Image",
            modifier = modifier
                .background(Red)
                .padding(10.dp)
                .background(Green)
                .padding(10.dp)
                .background(Blue)
                .padding(10.dp)
        )

    }
}

@Preview
@Composable
fun UserItem(
    user: User = User("Name", "imageUrl"),
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(10.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "Image",
            modifier = Modifier.size(50.dp)
                .clip(CircleShape)
        )
        Text(
            text = user.name,
            fontSize = 20.sp,
            modifier = Modifier
                .clickable { onClick() }
                .padding(20.dp)
        )
    }
}

data class User(val name: String, val imageUrl: String)
