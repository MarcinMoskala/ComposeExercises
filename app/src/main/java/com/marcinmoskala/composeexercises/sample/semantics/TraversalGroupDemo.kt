package com.marcinmoskala.composeexercises.sample.semantics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun TraversalGroupDemo() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        CardBox(
            "This sentence is in ",
            "the left column."
        )
        CardBox(
            "This sentence is ",
            "on the right."
        )
    }
}

@Composable
fun CardBox(
    topSampleText: String,
    bottomSampleText: String,
    modifier: Modifier = Modifier
) {
    Box(
//        modifier = modifier.semantics {
//            isTraversalGroup = true
//        }
    ) {
        Column {
            Text(topSampleText)
            Text(bottomSampleText)
        }
    }
}