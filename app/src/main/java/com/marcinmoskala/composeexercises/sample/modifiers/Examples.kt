package com.marcinmoskala.composeexercises.sample.modifiers

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marcinmoskala.composeexercises.R

@Preview
@Composable
private fun SizeExample() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier,
            contentDescription = null,
            painter = painterResource(id = R.drawable.avatar) // see check
        )
        Image(
            modifier = Modifier.size(300.dp),
            contentDescription = null,
            painter = painterResource(id = R.drawable.avatar)
        )
    }
}

@Preview
@Composable
private fun PaddingExample() {
    Column(
        modifier = Modifier
//            .width(160.dp)
    ) {
        Image(
            modifier = Modifier,
            contentDescription = null,
            painter = painterResource(id = R.drawable.check)
        )
        Image(
            modifier = Modifier.padding(40.dp),
            contentDescription = null,
            painter = painterResource(id = R.drawable.check)
        )
    }
}

@Preview
@Composable
private fun OffsetExample() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            modifier = Modifier,
            contentDescription = null,
            painter = painterResource(id = R.drawable.check)
        )
        Image(
            modifier = Modifier.offset(40.dp),
            contentDescription = null,
            painter = painterResource(id = R.drawable.check)
        )
    }
}

@Preview
@Composable
private fun ClipExample() {
    Column(
        modifier = Modifier
            .width(200.dp)
    ) {
        Image(
            modifier = Modifier.padding(10.dp),
            contentDescription = null,
            painter = painterResource(id = R.drawable.avatar)
        )
        Image(
            modifier = Modifier.padding(10.dp)
                .clip(shape = RoundedCornerShape(16.dp)),
            contentDescription = null,
            painter = painterResource(id = R.drawable.avatar)
        )
        Image(
            modifier = Modifier
                .padding(10.dp)
                .clip(shape = CircleShape),
            contentDescription = null,
            painter = painterResource(id = R.drawable.avatar)
        )
    }
}
@Preview
@Composable
private fun ClipExample2() {
    Column(
        modifier = Modifier
    ) {
        Text("SomeText\nSomeText\nSomeText\nSomeText",
            modifier = Modifier.padding(10.dp),
        )
        Text("SomeText\nSomeText\nSomeText\nSomeText",
            modifier = Modifier.padding(10.dp)
                .clip(CircleShape),
        )
    }
}

@Preview
@Composable
private fun BorderExample() {
    Column(
        modifier = Modifier
    ) {
        Text("No border",
            modifier = Modifier.padding(20.dp),
        )
        Text("Some border",
            modifier = Modifier.padding(10.dp)
                .border(1.dp, Color.Red)
                .padding(10.dp),
        )
        Text("Big border",
            modifier = Modifier.padding(10.dp)
                .border(3.dp, Color.Black)
                .padding(10.dp),
        )
    }
}


@Preview
@Composable
private fun Exercise() {
    Column(
        modifier = Modifier
    ) {
        Image(
            modifier = Modifier,
            contentDescription = null,
            painter = painterResource(id = R.drawable.avatar)
        )
    }
}

