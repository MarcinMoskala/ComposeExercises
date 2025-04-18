package com.marcinmoskala.composeexercises

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.marcinmoskala.composeexercises.sample.sample.GamePlot

class GameStoryDialogScreenScreenshots {

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
}