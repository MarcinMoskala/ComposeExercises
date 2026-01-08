@file:Suppress("ComposeRules")

package com.marcinmoskala.composeexercises.exercises.advanced

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

data class Product(val id: Int, val name: String, val price: Double)

object NaturalOrderComparator : Comparator<Product> {
    override fun compare(o1: Product, o2: Product): Int = 0
}

@Composable
fun ProductScreen(
    products: PersistentList<Product>,
    modifier: Modifier = Modifier,
    comparator: Comparator<Product> = NaturalOrderComparator,
) {
    RecompositionCounterEffect("ProductScreen")
    val resolvedListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val showBackToTop = resolvedListState.firstVisibleItemScrollOffset > 0

    val sortedProducts =
        products.sortedWith(comparator).toPersistentList()

    Box(modifier = modifier) {
        LazyColumn(state = resolvedListState) {
            items(sortedProducts) { product ->
                ProductItem(product)
            }
        }

        FirstPricePointer(
            scroll = resolvedListState.firstVisibleItemScrollOffset,
            modifier = Modifier.align(Alignment.TopEnd)
        )

        if (showBackToTop) {
            BackToTopButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(32.dp),
                onClick = { scope.launch { resolvedListState.scrollToItem(0) } }
            )
        }
    }
}

@Composable
private fun FirstPricePointer(
    scroll: Int,
    modifier: Modifier = Modifier
) {
    RecompositionCounterEffect("Header")
    Box(
        modifier = modifier
            .offset(y = with(LocalDensity.current) { (-scroll).toDp() })
            .padding(16.dp)
            .padding(top = 24.dp, end = 30.dp)
    ) {
        Text(
            text = "First price ↑",
            color = Color.Black
        )
    }
}

@Composable
private fun BackToTopButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    RecompositionCounterEffect("BackToTopButton")
    Text(
        text = "Back to top",
        color = Color.White,
        fontSize = 16.sp,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .background(Color.Blue)
            .padding(16.dp)
    )
}

@Composable
fun ProductItem(product: Product, modifier: Modifier = Modifier) {
    RecompositionCounterEffect("ProductItem_${product.id}")
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5F7FB))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = product.name,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF1B1F2B)
        )
        Text(
            text = "$${"%.2f".format(product.price)}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF4A5568)
        )
    }
}

@Preview
@Composable
private fun PreviewProductScreen() {
    val products = List(100) {
        Product(it, "Product $it", it * 10.0)
    }.toPersistentList()
    ProductScreen(products)
}

interface RecompositionCounter {
    fun increment(key: String)
    fun get(key: String): Int?
}

class ActualRecompositionCounter : RecompositionCounter {
    private var recompositions = ConcurrentHashMap<String, Int>()

    override fun increment(key: String) {
        recompositions.compute(key) { _, oldValue ->
            oldValue?.let { oldValue + 1 } ?: 1
        }
    }

    override fun get(key: String): Int? = recompositions[key]
}

object NoOpRecompositionCounter : RecompositionCounter {
    override fun increment(key: String) {}
    override fun get(key: String): Int? = null
}

val LocalCompositionCounter =
    staticCompositionLocalOf<RecompositionCounter> { NoOpRecompositionCounter }

@Composable
inline fun RecompositionCounterEffect(key: String) {
    val counter = LocalCompositionCounter.current
    SideEffect {
        counter.increment(key)
    }
}