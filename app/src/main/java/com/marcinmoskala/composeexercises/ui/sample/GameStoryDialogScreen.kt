package com.marcinmoskala.composeexercises.ui.sample

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marcinmoskala.composeexercises.R
import kotlinx.coroutines.delay

@Composable
fun GameStoryDialogScreen(
    dialogs: List<String>,
    onNext: () -> Unit,
) {
    var text: String? by remember { mutableStateOf(null) }
    LaunchedEffect(dialogs) {
        delay(1000)
        for (dialog in dialogs) {
            text = dialog
            delay(dialog.length * 70L + 1000)
        }
        text = null
    }
    GamePlot(text, onNext)
}

@Composable
fun GamePlot(
    text: String?,
    onPlay: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(R.drawable.wizard),
            contentDescription = "Wizard",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.Center)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            AnimatedContent(
                text,
                transitionSpec = {
                    scaleIn() + slideInVertically { it } togetherWith
                            scaleOut() + slideOutHorizontally { it }
                },
            ) {
                if (it != null) {
                    TextDialog(text = it)
                }
            }
            PlayButton(
                onPlay = onPlay,
            )
        }
    }
}

@Composable
private fun TextDialog(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
        color = Color.White,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(blueColor)
            .padding(24.dp)
    )
}

@Composable
private fun PlayButton(
    onPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onPlay,
        colors = buttonColors(
            containerColor = orangeColor,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier,
    ) {
        Text(
            text = "Continue",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

private val blueColor = Color(0xff2e90cf)
private val orangeColor = Color(0xffffa621)

@Preview(device = "id:pixel_xl")
@Preview(device = "id:desktop_medium")
@Composable
fun GamePlotNoTextPreview() {
    GamePlot(text = null, onPlay = {})
}

@Preview(device = "id:pixel_xl")
@Preview(device = "id:desktop_medium")
@Composable
fun GamePlotTextPreview() {
    GamePlot(
        text = "So you are the new pretender to become a master of Kotlin Coroutines?",
        onPlay = {})
}

@Preview(device = "id:pixel_xl")
@Preview(device = "id:desktop_medium")
@Composable
fun GamePlotScreenPreview() {
    GameStoryDialogScreen(
        dialogs = listOf(
            "So you are the new pretender to become a master of Kotlin Coroutines?",
            "Let's see how you do with basic structures..."
        ),
        onNext = {}
    )
}