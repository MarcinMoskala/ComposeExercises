package com.marcinmoskala.composeexercises

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.collections.component1
import kotlin.collections.component2

interface RecompositionCounter {
    fun increment(key: String)
    fun get(key: String): Int?
}

class ActualRecompositionCounter : RecompositionCounter {
    private val counts = mutableMapOf<String, Int>()

    override fun increment(key: String) {
        counts[key] = (counts[key] ?: 0) + 1
    }

    override fun get(key: String): Int? = counts[key]

    fun getCounts(): Map<String, Int> = counts.toMap()
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

@Composable
inline fun CountAndDisplayRecompositions(
    counter: ActualRecompositionCounter = remember { ActualRecompositionCounter() },
    crossinline content: @Composable () -> Unit
) {
    Column {
        Box(modifier = Modifier.weight(1f)) {
            CompositionLocalProvider(LocalCompositionCounter provides counter) {
                content()
            }
        }
        RecompositionCountersDisplay(counter)
    }
}

@Composable
fun RecompositionCountersDisplay(counter: ActualRecompositionCounter, modifier: Modifier = Modifier) {
    var counts by remember { mutableStateOf(emptyMap<String, Int>()) }
    LaunchedEffect(counter) {
        while (true) {
            counts = counter.getCounts()
                .toList().sortedBy { it.first }.toMap()
            delay(1000)
        }
    }
    Column(modifier = modifier) {
        counts.forEach { (key, count) ->
            Text(
                text = "Recompositions of $key: $count",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                fontSize = 16.sp,
                color = Color.Red
            )
        }
    }
}