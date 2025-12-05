package com.marcinmoskala.composeexercises.sample.theming

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

data class AnalyticsConfig(
    val isEnabled: Boolean = true,
    val trackScreenViews: Boolean = true,
    val trackClicks: Boolean = true,
    val screenCategory: String = "general"
)

@Composable
fun Analytics(
    isEnabled: Boolean? = null,
    trackScreenViews: Boolean? = null,
    trackClicks: Boolean? = null,
    screenCategory: String? = null,
    content: @Composable () -> Unit
) {
    // TODO: Provide the AnalyticsConfig to the composition local
    // update values from the current AnalyticsConfig
    content()
}

@Composable
@Preview
fun AnalyticsConfigPreview() {
    Column {
        Analytics(screenCategory = "header", trackClicks = false) {
            AnalyticsConfigDebugger(name = "Top")
        }
        Analytics(screenCategory = "body") {
            AnalyticsConfigDebugger(name = "User Name")
            Analytics(trackClicks = false) {
                AnalyticsConfigDebugger(name = "Password")
            }
        }
        Analytics(isEnabled = false) {
            Analytics(screenCategory = "footer") {
                AnalyticsConfigDebugger(name = "Bottom")
            }
            Analytics(screenCategory = "second_footer") {
                AnalyticsConfigDebugger(name = "Bottom 2")
            }
        }
    }
}

@Composable
fun AnalyticsConfigDebugger(name: String) {
    val config: AnalyticsConfig = AnalyticsConfig() // TODO: Replace with AnalyticsConfig from CompositionLocal
    Text(text = "Analytics config for $name", style = MaterialTheme.typography.headlineMedium)
    Text(text = "enabled: ${config.isEnabled}" , style = MaterialTheme.typography.bodyMedium)
    Text(text = "trackScreenViews: ${config.trackScreenViews}", style = MaterialTheme.typography.bodyMedium)
    Text(text = "trackClicks: ${config.trackClicks}", style = MaterialTheme.typography.bodyMedium)
    Text(text = "screenCategory: ${config.screenCategory}", style = MaterialTheme.typography.bodyMedium)
}