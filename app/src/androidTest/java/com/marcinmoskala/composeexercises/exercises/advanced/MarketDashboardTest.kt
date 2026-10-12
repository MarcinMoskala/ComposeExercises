package com.marcinmoskala.composeexercises.exercises.advanced

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Recomposition tests for [MarketDashboardScreen].
 *
 * The test clock is driven manually (`autoAdvance = false`), so every test
 * observes exactly the frames it advances. The simulator never ticks on its
 * own (`tickMillis = Long.MAX_VALUE`); tests call [MarketSimulator.tick] when
 * they want the market to move.
 */
class MarketDashboardTest {

    private companion object {
        const val DRAG_FRAMES = 20
        const val DRAG_PX_PER_FRAME = 12f
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    private val counter = ActualRecompositionCounter()

    @Test
    fun idleScreenDoesNotRecomposeEveryFrame() {
        setScreen(manualSimulator())

        val screen = recompositionsDuring("MarketDashboardScreen") { advanceTime(2_000) }

        assertTrue(
            "MarketDashboardScreen recomposed $screen times during 2 seconds without any data change. " +
                "Nothing visible in it changes more often than once per second.",
            screen <= 2,
        )
    }

    @Test
    fun idleHeaderDoesNotRecompose() {
        setScreen(manualSimulator())

        val header = recompositionsDuring("MarketHeader") { advanceTime(2_000) }

        assertEquals(
            "MarketHeader recomposed $header times during 2 seconds of idling. " +
                "Fast-changing values should be read as close to where they are used as possible.",
            0,
            header,
        )
    }

    @Test
    fun pulsingLiveIndicatorDoesNotRecompose() {
        setScreen(manualSimulator())

        val liveIndicator = recompositionsDuring("LiveIndicator") { advanceTime(2_000) }

        assertEquals(
            "LiveIndicator recomposed $liveIndicator times during 2 seconds of idling. " +
                "An animation that only changes how something is drawn does not need composition.",
            0,
            liveIndicator,
        )
    }

    @Test
    fun marketTickDoesNotRecomposeRowsOfUnchangedStocks() {
        val simulator = manualSimulator(stockCount = 6, updatesPerTick = 1)
        setScreen(simulator)
        val pricesBefore = simulator.state.value.stocks.associate { it.symbol to it.price }

        val deltas = rowRecompositionsDuring(simulator) {
            composeTestRule.runOnIdle { simulator.tick() }
            advanceTime(1_000)
        }

        val unchanged = simulator.state.value.stocks
            .filter { it.price == pricesBefore[it.symbol] }
            .map { it.symbol }
        assertTrue("Expected some stocks not to change in this tick", unchanged.isNotEmpty())
        val recomposedUnchanged = unchanged.filter { (deltas[it] ?: 0) > 0 }
        assertTrue(
            "Rows of stocks whose price did not change recomposed after a market tick: " +
                recomposedUnchanged.joinToString { "$it (${deltas[it]}x)" },
            recomposedUnchanged.isEmpty(),
        )
    }

    @Test
    fun priceFlashDoesNotRecomposeRowEveryFrame() {
        val simulator = manualSimulator(stockCount = 6, updatesPerTick = 1)
        setScreen(simulator)
        val pricesBefore = simulator.state.value.stocks.associate { it.symbol to it.price }

        val deltas = rowRecompositionsDuring(simulator) {
            composeTestRule.runOnIdle { simulator.tick() }
            advanceTime(1_000)
        }

        val changed = simulator.state.value.stocks
            .single { it.price != pricesBefore[it.symbol] }
            .symbol
        val changedRow = deltas[changed] ?: 0
        assertTrue(
            "Row of $changed recomposed $changedRow times after a single price change. " +
                "It should recompose once for the new price, not on every frame of the flash animation.",
            changedRow <= 1,
        )
    }

    @Test
    fun reorderingListDoesNotRecomposeRows() {
        val simulator = manualSimulator(stockCount = 6)
        setScreen(simulator)

        val deltas = rowRecompositionsDuring(simulator) {
            composeTestRule.onNodeWithText("Price").performClick()
            advanceTime(1_000)
        }

        val recomposed = deltas.filterValues { it > 0 }
        assertTrue(
            "Changing sort order should only move rows, but these rows recomposed: " +
                recomposed.entries.joinToString { "${it.key} (${it.value}x)" },
            recomposed.isEmpty(),
        )
    }

    @Test
    fun scrollingDoesNotRecomposeScreenOrHeaderEveryFrame() {
        setScreen(manualSimulator())

        val screen = recompositionsCausedByScrolling("MarketDashboardScreen")
        val header = recompositionsCausedByScrolling("MarketHeader")

        assertTrue(
            "MarketDashboardScreen recomposed $screen times while scrolling the list (20 frames). " +
                "Scroll position should not be read in composition.",
            screen <= 2,
        )
        assertTrue(
            "MarketHeader recomposed $header times while it was collapsing (20 frames). " +
                "Collapsing can be done in layout or drawing phase.",
            header <= 2,
        )
    }

    @Test
    fun scrollingFarDownDoesNotRecomposeScreen() {
        setScreen(manualSimulator())
        composeTestRule.onNodeWithTag("stockList").performScrollToIndex(10)
        advanceTime(1_000)

        val screen = recompositionsCausedByScrolling("MarketDashboardScreen")
        val button = recompositionsCausedByScrolling("ScrollToTopButton")

        assertEquals(
            "MarketDashboardScreen recomposed $screen times while scrolling, " +
                "even though the scroll-to-top button visibility did not change.",
            0,
            screen,
        )
        assertEquals("ScrollToTopButton recomposed $button times while scrolling.", 0, button)
    }

    private fun manualSimulator(stockCount: Int = 60, updatesPerTick: Int = 6) = MarketSimulator(
        stockCount = stockCount,
        tickMillis = Long.MAX_VALUE,
        updatesPerTick = updatesPerTick,
    )

    private fun setScreen(simulator: MarketSimulator) {
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            CompositionLocalProvider(LocalCompositionCounter provides counter) {
                MarketDashboardScreen(simulator = simulator)
            }
        }
        // Let initial effects and animations settle
        advanceTime(1_000)
    }

    private fun advanceTime(millis: Long) {
        composeTestRule.mainClock.advanceTimeBy(millis)
        composeTestRule.waitForIdle()
    }

    /**
     * Recompositions of [key] during a slow 20-frame drag of the list, minus
     * recompositions during the same number of frames without touching the
     * screen, so that only the effect of scrolling is measured.
     */
    private fun recompositionsCausedByScrolling(key: String): Int {
        val idle = recompositionsDuring(key) { runFrames(DRAG_FRAMES) {} }
        val list = composeTestRule.onNodeWithTag("stockList")
        val scrolling = recompositionsDuring(key) {
            list.performTouchInput { down(center) }
            runFrames(DRAG_FRAMES) {
                list.performTouchInput { moveBy(Offset(0f, -DRAG_PX_PER_FRAME)) }
            }
            list.performTouchInput { up() }
        }
        return scrolling - idle
    }

    private inline fun runFrames(frames: Int, perFrame: () -> Unit) {
        repeat(frames) {
            perFrame()
            composeTestRule.mainClock.advanceTimeByFrame()
            composeTestRule.waitForIdle()
        }
        advanceTime(1_000)
    }

    private fun count(key: String): Int = composeTestRule.runOnIdle { counter.get(key) ?: 0 }

    private inline fun recompositionsDuring(key: String, action: () -> Unit): Int {
        val before = count(key)
        action()
        return count(key) - before
    }

    private inline fun rowRecompositionsDuring(
        simulator: MarketSimulator,
        action: () -> Unit,
    ): Map<String, Int> {
        val symbols = simulator.state.value.stocks.map { it.symbol }
        val before = symbols.associateWith { count("StockRow_$it") }
        action()
        return symbols.associateWith { count("StockRow_$it") - before.getValue(it) }
    }
}
