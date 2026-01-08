package com.marcinmoskala.composeexercises.exercises.advanced

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.compose.ui.unit.dp
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
    fun productScreenRecompositionDoesNotResortWhenInputsStable() {
        val counter = ActualRecompositionCounter()
        var products by mutableStateOf(
            persistentListOf(
                Product(1, "Bread", 2.0),
                Product(2, "Milk", 3.0),
                Product(3, "Juice", 1.5)
            )
        )
        var modifierToggle by mutableStateOf(false)
        var comparisons = 0
        val countingComparator = Comparator<Product> { a, b ->
            comparisons++
            a.price.compareTo(b.price)
        }

        composeTestRule.setContent {
            CompositionLocalProvider(LocalCompositionCounter provides counter) {
                val modifier = if (modifierToggle) {
                    Modifier.padding(8.dp)
                } else {
                    Modifier.padding(4.dp)
                }
                ProductScreen(
                    products = products,
                    comparator = countingComparator,
                    modifier = modifier
                )
            }
        }

        composeTestRule.waitForIdle()
        val initialComparisons = comparisons
        val initialProductScreenCount = counter.get("ProductScreen") ?: 0
        assertTrue("Initial sort should run at least once", initialComparisons >= 1)
        assertEquals("ProductScreen should compose once initially", 1, initialProductScreenCount)

        composeTestRule.runOnUiThread { modifierToggle = true }
        composeTestRule.waitForIdle()

        val afterToggleProductScreenCount = counter.get("ProductScreen") ?: 0
        assertTrue(
            "ProductScreen should recompose when modifier changes",
            afterToggleProductScreenCount > initialProductScreenCount
        )
        assertEquals(
            "Changing modifier should not re-run sorting when products and comparator stay the same",
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
        val products = (1..50).map { i -> Product(i, "Item $i", price = i.toDouble()) }.toPersistentList()

        composeTestRule.setContent {
            CompositionLocalProvider(LocalCompositionCounter provides counter) {
                ProductScreen(products = products)
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNode(hasScrollAction())
            .performScrollToIndex(10)
        composeTestRule.waitForIdle()

        composeTestRule.onNode(hasScrollAction())
            .performTouchInput {
                val start = center
                swipe(start, start.copy(y = start.y - 120f))
            }
        composeTestRule.waitForIdle()

        val afterFirstScrollProductScreen = counter.get("ProductScreen") ?: 0
        val backToTopAfterFirstScroll = counter.get("BackToTopButton") ?: 0
        assertTrue(
            "BackToTopButton should compose after scrolling down; visibility turning true should happen once when crossing the threshold",
            backToTopAfterFirstScroll >= 1
        )

        composeTestRule.onNode(hasScrollAction())
            .performTouchInput {
                val start = center
                swipe(start, start.copy(y = start.y - 120f))
            }
        composeTestRule.waitForIdle()

        val afterSecondScrollProductScreen = counter.get("ProductScreen") ?: 0
        val backToTopAfterSecondScroll = counter.get("BackToTopButton") ?: 0

        assertEquals(
            "Further scrolling while BackToTopButton remains visible should not recompose ProductScreen; removing derivedStateOf around showBackToTop would increase this count",
            afterFirstScrollProductScreen,
            afterSecondScrollProductScreen
        )
        assertEquals(
            "BackToTopButton should not recompose again while its visibility state stays true",
            backToTopAfterFirstScroll,
            backToTopAfterSecondScroll
        )
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
