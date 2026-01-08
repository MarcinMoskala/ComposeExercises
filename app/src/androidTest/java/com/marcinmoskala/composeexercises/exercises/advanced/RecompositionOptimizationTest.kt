package com.marcinmoskala.composeexercises.exercises.advanced

import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performScrollToIndex
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class RecompositionOptimizationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun sortingRunsOnlyWhenProductsChange() {
        val counter = ActualRecompositionCounter()
        var products by mutableStateOf(
            persistentListOf(
                Product(1, "Bread", 2.0),
                Product(2, "Milk", 3.0)
            )
        )
        var trigger by mutableStateOf(false)
        var comparisons = 0

        val countingComparator = Comparator<Product> { a, b ->
            comparisons++
            a.price.compareTo(b.price)
        }

        composeTestRule.setContent {
            CompositionLocalProvider(LocalCompositionCounter provides counter) {
                // Unrelated state change should not trigger another sort
                if (trigger) {
                    Text("trigger")
                } else {
                    Text("idle")
                }
                ProductScreen(
                    products = products,
                    comparator = countingComparator
                )
            }
        }

        assertEquals("ProductScreen should compose exactly once on first render", 1, counter.get("ProductScreen") ?: 0)
        assertEquals("Header should compose exactly once on first render", 1, counter.get("Header") ?: 0)
        assertEquals("Product 1 should compose exactly once on first render", 1, counter.get("ProductItem_1") ?: 0)
        assertEquals("Product 2 should compose exactly once on first render", 1, counter.get("ProductItem_2") ?: 0)
        assertEquals("Sorting should run once on initial composition", 1, comparisons)

        composeTestRule.runOnUiThread {
            trigger = true
        }
        composeTestRule.waitForIdle()

        assertEquals("Unrelated state change should not recompose ProductScreen", 1, counter.get("ProductScreen") ?: 0)
        assertEquals("Unrelated state change should not recompose Header", 1, counter.get("Header") ?: 0)
        assertEquals("Unrelated state change should not recompose Product 1", 1, counter.get("ProductItem_1") ?: 0)
        assertEquals("Unrelated state change should not recompose Product 2", 1, counter.get("ProductItem_2") ?: 0)
        assertEquals("Unrelated state change should not trigger additional sorting", 1, comparisons)

        composeTestRule.runOnUiThread {
            products = persistentListOf(
                Product(1, "Bread", 2.5),
                Product(2, "Milk", 3.0)
            )
        }
        composeTestRule.waitForIdle()

        val productScreenCount = counter.get("ProductScreen") ?: 0
        val headerCount = counter.get("Header") ?: 0
        val item1Count = counter.get("ProductItem_1") ?: 0
        val item2Count = counter.get("ProductItem_2") ?: 0

        // Product changes should allow at most one extra recomposition & resort
        assertTrue("ProductScreen recomposed too often after product update", productScreenCount in 1..2)
        assertTrue("Header recomposed too often after product update", headerCount in 1..2)
        assertTrue("Product 1 recomposed too often after product update", item1Count in 1..2)
        assertTrue("Product 2 recomposed too often after product update", item2Count in 1..2)
        assertTrue("Sorting ran too many times after product update", comparisons in 1..2)
    }

    @Test
    fun unrelatedStateChangeDoesNotResortProducts() {
        var products by mutableStateOf(
            persistentListOf(
                Product(1, "Bread", 2.0),
                Product(2, "Milk", 3.0)
            )
        )
        var flag by mutableStateOf(false)
        var comparisons = 0
        val countingComparator = Comparator<Product> { a, b ->
            comparisons++
            a.price.compareTo(b.price)
        }

        composeTestRule.setContent {
            if (flag) {
                Text("flag on")
            } else {
                Text("flag off")
            }
            ProductScreen(
                products = products,
                comparator = countingComparator
            )
        }

        composeTestRule.waitForIdle()
        val initialComparisons = comparisons
        assertTrue("Initial sort should run at least once", initialComparisons >= 1)

        composeTestRule.runOnUiThread { flag = !flag }
        composeTestRule.waitForIdle()
        composeTestRule.runOnUiThread { flag = !flag }
        composeTestRule.waitForIdle()

        assertEquals(
            "Unrelated state change should not re-run sorting; removing remember on sortedProducts would increase comparisons",
            initialComparisons,
            comparisons
        )
    }

    @Test
    fun productsChangeTriggersResort() {
        var products by mutableStateOf(
            persistentListOf(
                Product(1, "Bread", 2.0),
                Product(2, "Milk", 3.0),
                Product(3, "Juice", 1.5)
            )
        )
        var comparisons = 0
        val countingComparator = Comparator<Product> { a, b ->
            comparisons++
            a.price.compareTo(b.price)
        }

        composeTestRule.setContent {
            ProductScreen(
                products = products,
                comparator = countingComparator
            )
        }

        composeTestRule.waitForIdle()
        val initialComparisons = comparisons
        assertTrue("Initial sort should run at least once", initialComparisons >= 1)

        composeTestRule.runOnUiThread {
            // Change price order to ensure resort is needed
            products = persistentListOf(
                Product(1, "Bread", 5.0),
                Product(2, "Milk", 3.0),
                Product(3, "Juice", 1.0)
            )
        }
        composeTestRule.waitForIdle()

        assertTrue(
            "Product changes should trigger a resort; missing remember keys would keep comparisons at $initialComparisons",
            comparisons > initialComparisons
        )
        assertTrue(
            "Resort should not run excessively",
            comparisons - initialComparisons <= products.size
        )
    }

    @Test
    fun derivedStateOfLimitsRecomposition() {
        val counter = ActualRecompositionCounter()
        val products = persistentListOf(
            Product(1, "Bread", 2.0),
            Product(2, "Milk", 3.0)
        )

        composeTestRule.setContent {
            CompositionLocalProvider(LocalCompositionCounter provides counter) {
                ProductScreen(
                    products = products
                )
            }
        }

        assertEquals("ProductScreen should compose once initially", 1, counter.get("ProductScreen") ?: 0)
        assertEquals("Header should compose once initially", 1, counter.get("Header") ?: 0)

        composeTestRule.onNode(hasScrollAction())
            .performScrollToIndex(1)

        val afterFirstScrollProductScreen = counter.get("ProductScreen") ?: 0
        val afterFirstScrollHeader = counter.get("Header") ?: 0
        assertEquals("ProductScreen should remain composed once when offset-only scrolling", 1, afterFirstScrollProductScreen)
        assertEquals("Header should remain composed once when offset-only scrolling", 1, afterFirstScrollHeader)
        assertEquals("BackToTopButton should not be composed when offset is zero at target index", 0, counter.get("BackToTopButton") ?: 0)

        // Further scroll within the same derivedState bucket should not recompose
        composeTestRule.onNode(hasScrollAction())
            .performScrollToIndex(1)

        assertEquals(
            "Repeated scroll within same bucket should not recompose ProductScreen again",
            afterFirstScrollProductScreen,
            counter.get("ProductScreen") ?: 0
        )
        assertEquals(
            "Repeated scroll within same bucket should not recompose Header again",
            afterFirstScrollHeader,
            counter.get("Header") ?: 0
        )
        assertEquals(
            "BackToTopButton should remain not composed without extra recompositions",
            0,
            counter.get("BackToTopButton") ?: 0
        )

        composeTestRule.onNode(hasScrollAction())
            .performScrollToIndex(0)

        val afterReturnToTopProductScreen = counter.get("ProductScreen") ?: 0
        val afterReturnToTopHeader = counter.get("Header") ?: 0
        assertEquals("Returning to top should not trigger recomposition when offset returns to zero", afterFirstScrollProductScreen, afterReturnToTopProductScreen)
        assertEquals("Header should not recompose when returning to zero offset", afterFirstScrollHeader, afterReturnToTopHeader)
        assertEquals("BackToTopButton remains not composed across the scroll cycle", 0, counter.get("BackToTopButton") ?: 0)
    }

    @Test
    fun stableKeysKeepItemsAlignedOnInsertion() {
        val counter = ActualRecompositionCounter()
        var products by mutableStateOf(
            persistentListOf(
                Product(101, "Bread", 2.0),
                Product(202, "Milk", 3.0),
                Product(303, "Cheese", 4.0)
            )
        )
        composeTestRule.setContent {
            CompositionLocalProvider(LocalCompositionCounter provides counter) {
                ProductScreen(
                    products = products,
                    comparator = Comparator { a, b -> a.price.compareTo(b.price) }
                )
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNode(hasScrollAction())
            .performScrollToIndex(2)
        composeTestRule.waitForIdle()

        assertEquals("Product 101 should compose once initially", 1, counter.get("ProductItem_101") ?: 0)
        assertEquals("Product 202 should compose once initially", 1, counter.get("ProductItem_202") ?: 0)
        assertEquals("Product 303 should compose once initially", 1, counter.get("ProductItem_303") ?: 0)

        composeTestRule.runOnUiThread {
            products = (persistentListOf(Product(999, "New arrival", 1.0)) + products).toPersistentList()
        }
        composeTestRule.waitForIdle()

        assertEquals(
            "Stable keys should prevent unnecessary recomposition of unchanged products when inserting at the top",
            1,
            counter.get("ProductItem_202") ?: 0
        )
        assertEquals(
            "Stable keys should prevent unnecessary recomposition of unchanged products when inserting at the top",
            1,
            counter.get("ProductItem_303") ?: 0
        )
    }

    @Test
    fun offsetLambdaAndValueProviderAvoidRecomposition() {
        val counter = ActualRecompositionCounter()
        val products = (1..30).map { i -> Product(i, "Item $i", price = i.toDouble()) }.toPersistentList()

        composeTestRule.setContent {
            CompositionLocalProvider(LocalCompositionCounter provides counter) {
                ProductScreen(products = products)
            }
        }

        composeTestRule.waitForIdle()
        assertEquals("ProductScreen should compose once initially", 1, counter.get("ProductScreen") ?: 0)
        assertEquals("Header should compose once initially", 1, counter.get("Header") ?: 0)

        composeTestRule.onNode(hasScrollAction())
            .performScrollToIndex(5)
        composeTestRule.waitForIdle()

        val productScreenAfterFirstScroll = counter.get("ProductScreen") ?: 0
        val headerAfterFirstScroll = counter.get("Header") ?: 0
        assertEquals("ProductScreen should stay composed once; offset lambda prevents recomposition", 1, productScreenAfterFirstScroll)
        assertEquals("Header should stay composed once; value provider avoids recomposition", 1, headerAfterFirstScroll)

        composeTestRule.onNode(hasScrollAction())
            .performScrollToIndex(6)
        composeTestRule.waitForIdle()

        val productScreenAfterSecondScroll = counter.get("ProductScreen") ?: 0
        val headerAfterSecondScroll = counter.get("Header") ?: 0
        assertEquals(
            "ProductScreen should not recompose across further scrolls",
            productScreenAfterFirstScroll,
            productScreenAfterSecondScroll
        )
        assertEquals(
            "Header should not recompose again when offset is calculated in layout using a lambda",
            headerAfterFirstScroll,
            headerAfterSecondScroll
        )
    }
}
