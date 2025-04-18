package com.marcinmoskala.composeexercises.sample.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextPage(
    text: String,
    onNextPage: (() -> Unit)? = null,
    onPreviousPage: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Text(
            text = text,
            modifier = Modifier.fillMaxSize(),
            fontSize = 20.sp,
        )
        if (onPreviousPage != null) {
            Button(
                onClick = onPreviousPage,
                modifier = Modifier.padding(16.dp)
                    .align(Alignment.BottomStart)
            ) {
                Text("Previous")
            }
        }
        if (onNextPage != null) {
            Button(
                onClick = onNextPage,
                modifier = Modifier.padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Text("Next")
            }
        }
    }
}

@Preview
@Composable
fun TextPageNoButtonsPreview() {
    TextPage(
        text = LoremIpsum(100).values.first()
    )
}

@Preview
@Composable
fun TextPageBothButtonsPreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    TextPage(
        text = text,
        onNextPage = {},
        onPreviousPage = {}
    )
}

@Preview
@Composable
fun TextNextButtonPreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    TextPage(
        text = text,
        onNextPage = {},
    )
}

@Preview
@Composable
fun TextPrevButtonPreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    TextPage(
        text = text,
        onPreviousPage = {}
    )
}