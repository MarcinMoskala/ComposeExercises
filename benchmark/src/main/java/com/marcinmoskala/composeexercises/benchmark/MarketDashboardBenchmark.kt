package com.marcinmoskala.composeexercises.benchmark

import android.content.Intent
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Records Perfetto traces of MarketDashboardScreen.
 *
 * Run: ./gradlew :benchmark:connectedBenchmarkAndroidTest
 *
 * Every iteration produces a `.perfetto-trace` file in
 * benchmark/build/outputs/connected_android_test_additional_output/benchmark/connected/<device>/
 * that can be opened in https://ui.perfetto.dev or in Android Studio (Profiler -> Load from file).
 * Results (frame times, recomposition counts) are printed in the test output and saved as JSON
 * in the same directory. Compare them before and after the optimization.
 */
@OptIn(ExperimentalMetricApi::class)
@RunWith(AndroidJUnit4::class)
class MarketDashboardBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    // Can be overridden with instrumentation argument `-e iterations N`
    private val iterations = InstrumentationRegistry.getArguments()
        .getString("iterations")?.toInt() ?: 3

    /** Screen is open and the market is ticking, the user does not touch it. */
    @Test
    fun idle() = benchmarkRule.measureRepeated(
        packageName = PACKAGE_NAME,
        metrics = metrics(),
        iterations = iterations,
        // WARM, because composition tracing fails to enable on cold start (tracing-perfetto 1.0.1)
        startupMode = StartupMode.WARM,
        setupBlock = { startMarketDashboard() },
    ) {
        // Fixed measurement window: the app never becomes idle (clock, animations),
        // so waiting for idle would make the window depend on the measured code.
        Thread.sleep(5_000)
    }

    /** User scrolls the list up and down. */
    @Test
    fun scroll() = benchmarkRule.measureRepeated(
        packageName = PACKAGE_NAME,
        metrics = metrics(),
        iterations = iterations,
        // WARM, because composition tracing fails to enable on cold start (tracing-perfetto 1.0.1)
        startupMode = StartupMode.WARM,
        setupBlock = { startMarketDashboard() },
    ) {
        val list = device.findObject(By.res("stockList"))
        list.setGestureMargin(device.displayWidth / 5)
        // fling() waits until scrolling finishes
        repeat(3) { list.fling(Direction.DOWN) }
        repeat(3) { list.fling(Direction.UP) }
    }

    private fun MacrobenchmarkScope.startMarketDashboard() {
        startActivityAndWait(
            Intent().setClassName(PACKAGE_NAME, "$PACKAGE_NAME.exercises.advanced.MarketDashboardActivity")
        )
        device.wait(Until.hasObject(By.res("stockList")), 5_000)
    }

    private fun metrics() = listOf(FrameTimingMetric()) + threadMetrics() + compositionMetrics()

    /**
     * Total time (and count) of work per frame phase, works without composition tracing:
     * - mainFrame: whole frame on the main thread (input, animations, composition, layout, draw recording)
     * - recompose: composition
     * - traversal: measure, layout and recording draw commands
     * - renderThread: executing draw commands on RenderThread
     */
    private fun threadMetrics() = listOf(
        "mainFrame" to "Choreographer#doFrame%",
        // Nested inside doFrame; subtract from mainFrame to avoid counting it twice
        "mainFrameResync" to "Choreographer#doFrame - resynced%",
        "recompose" to "Recomposer:recompose",
        "traversal" to "traversal",
        "renderThread" to "DrawFrames%",
    ).map { (label, section) ->
        TraceSectionMetric(section, label = label, mode = TraceSectionMetric.Mode.Sum, targetPackageOnly = true)
    }

    /**
     * How many times each composable was (re)composed, and how long it took in total.
     * Composition tracing names sections like `com.example.StockRow (MarketDashboard.kt:123)`.
     */
    private fun compositionMetrics() = TRACED_COMPOSABLES.map { name ->
        // Mode.Sum reports both <name>Count and <name>SumMs
        TraceSectionMetric("%.$name (%", label = name, mode = TraceSectionMetric.Mode.Sum, targetPackageOnly = true)
    }

    private companion object {
        const val PACKAGE_NAME = "com.marcinmoskala.composeexercises"
        val TRACED_COMPOSABLES = listOf("MarketDashboardScreen", "MarketHeader", "LiveIndicator", "StockRow")
    }
}
