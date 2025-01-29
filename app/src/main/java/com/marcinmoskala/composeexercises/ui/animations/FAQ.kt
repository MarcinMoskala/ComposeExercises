package com.marcinmoskala.composeexercises.ui.animations

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
private fun FrequentlyAskedQuestionsPage() {
    LazyColumn {
        faq.forEach { (question, answer) ->
            item {
                FAQItem(question, answer)
            }
        }
    }
}

private val faq = mapOf(
    "Example question 1" to LoremIpsum(100).values.first().replace("\n", " "),
    "Example question 2" to LoremIpsum(100).values.first().replace("\n", " "),
    "Example question 3" to LoremIpsum(100).values.first().replace("\n", " "),
    "Example question 4" to LoremIpsum(100).values.first().replace("\n", " "),
)

@Composable
private fun FAQItem(question: String, answer: String) {
    var opened by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { opened = !opened }
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = question,
                    fontSize = 20.sp,
                    // bold
                )
                Image(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(if (opened) 180f else 0f)
                )
            }
            Text(
                text = answer,
                fontSize = 14.sp,
                maxLines = if (opened) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}