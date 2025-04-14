package com.marcinmoskala.composeexercises.ui.modifiers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marcinmoskala.composeexercises.R
import com.marcinmoskala.composeexercises.loremIpsum

@Preview
@Composable
private fun TextsExample() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Cyan)
                .padding(10.dp)
                .width(IntrinsicSize.Min)
        ) {
            DecoratedText("Good day!")
            DecoratedText("Future")
            DecoratedText("Compose Masters!")
        }
    }
}

@Composable
private fun DecoratedText(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        textAlign = TextAlign.Center,
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Blue)
            .padding(10.dp)

    )
}

@Composable
private fun RowExample(name: String, label: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
    ) {
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "Picture of a user",
            modifier = Modifier
                .padding(8.dp)
                .size(80.dp)
                .clip(CircleShape)
        )
        VerticalDivider(
            color = Color.Black,
            modifier = Modifier
//                .height(80.dp)
                .padding(vertical = 8.dp)
                .fillMaxHeight()
                .width(1.dp)
        )
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(name, fontSize = 20.sp)
            Text(label)
        }
    }
}

@Preview
@Composable
private fun RowExamplePreview() {
    Column {
        RowExample("John Foo", "Coworker")
        RowExample(loremIpsum(50), "Coworker")
    }
}