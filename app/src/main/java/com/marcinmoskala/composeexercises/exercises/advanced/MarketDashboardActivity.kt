package com.marcinmoskala.composeexercises.exercises.advanced

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId

/**
 * Hosts [MarketDashboardScreen], so it can be started directly for profiling:
 * `adb shell am start -n com.marcinmoskala.composeexercises/.exercises.advanced.MarketDashboardActivity`
 */
class MarketDashboardActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                // Exposes test tags to UiAutomator used by Macrobenchmark
                MarketDashboardScreen(Modifier.semantics { testTagsAsResourceId = true })
            }
        }
    }
}
