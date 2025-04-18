package com.marcinmoskala.composeexercises.sample.animations

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FrequentlyAskedQuestionsPage(state: FAQState, toggleOpen: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        state.items.forEachIndexed { i, (question, answer, isOpen) ->
            item {
                FAQItem(question, answer, isOpen, { toggleOpen(i) })
            }
        }
    }
}

data class FAQState(val items: List<FAQItemState>)

data class FAQItemState(val question: String, val answer: String, val isOpen: Boolean = false)

@Composable
private fun FAQItem(question: String, answer: String, isOpen: Boolean, toggleOpen: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { toggleOpen(isOpen) }
            .padding(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
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
                        .rotate(if (isOpen) 180f else 0f)
                )
            }
            Text(
                text = answer,
                fontSize = 14.sp,
                maxLines = if (isOpen) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )
            if(isOpen) {
                Button(onClick = {}, modifier = Modifier.padding(top = 4.dp)) {
                    Text("Need more help? Contact us")
                }
            }
        }
    }
}

@Preview
@Composable
private fun FrequentlyAskedQuestionsPagePreview() {
    var state by remember { mutableStateOf(FAQState(faq.map { (q, a) -> FAQItemState(q, a) })) }
    FrequentlyAskedQuestionsPage(state, { i -> state = state.copy(items = state.items.mapIndexed { index, item -> if (index == i) item.copy(isOpen = !item.isOpen) else item }) })
}

@Preview
@Composable
private fun FrequentlyAskedQuestionsPageOpenedPreview() {
    var state by remember { mutableStateOf(FAQState(faq.map { (q, a) -> FAQItemState(q, a, true) })) }
    FrequentlyAskedQuestionsPage(state, { i -> state = state.copy(items = state.items.mapIndexed { index, item -> if (index == i) item.copy(isOpen = !item.isOpen) else item }) })
}

private val faq = mapOf(
    "Example question 1" to LoremIpsum(100).values.first().replace("\n", " "),
    "Example question 2" to LoremIpsum(100).values.first().replace("\n", " "),
    "Example question 3" to LoremIpsum(100).values.first().replace("\n", " "),
    "Example question 4" to LoremIpsum(100).values.first().replace("\n", " "),
)