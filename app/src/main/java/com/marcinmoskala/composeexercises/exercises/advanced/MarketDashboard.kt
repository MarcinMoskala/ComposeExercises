@file:Suppress("ComposeRules")

package com.marcinmoskala.composeexercises.exercises.advanced

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random

// Exercise: Live market dashboard.
//
// This screen works correctly, but it is slow: it janks while scrolling and
// keeps the main thread and RenderThread busy even when the user does nothing.
//
// 1. Run the app on a real device (release-like build, not a debug preview),
//    record a system trace with Perfetto (or Android Studio Profiler ->
//    "Capture System Activities") while idling and while scrolling the list.
//    Find which composables recompose too often, which work is done on every
//    frame, and what the RenderThread is spending its time on.
// 2. Fix the issues you have found, without changing how the screen looks or
//    behaves, and record a new trace to confirm the improvement.
//
// Use RecompositionCounterEffect or Layout Inspector recomposition counts to
// double-check your findings.

data class Stock(
    val symbol: String,
    val name: String,
    val price: Double,
    val previousPrice: Double,
    val openPrice: Double,
    val history: List<Double>,
    val volume: Long,
    val lastUpdated: Long,
)

data class MarketState(
    val stocks: List<Stock>,
    val tick: Long,
    val favorites: Set<String>,
)

data class MarketSummary(
    val totalValue: Double,
    val gainers: Int,
    val losers: Int,
    val averageVolatility: Double,
    val mostCorrelated: String,
)

enum class SortMode(val label: String, val comparator: Comparator<Stock>) {
    Symbol("Symbol", compareBy { it.symbol }),
    Price("Price", compareByDescending { it.price }),
    Change("Change", compareByDescending { it.changeFromOpenPercent }),
    Volume("Volume", compareByDescending { it.volume }),
}

val Stock.changeFromOpenPercent: Double
    get() = (price - openPrice) / openPrice * 100

class MarketSimulator(
    private val stockCount: Int = 60,
    private val historySize: Int = 120,
    private val tickMillis: Long = 100,
    private val updatesPerTick: Int = 6,
) {
    private val random = Random(42)
    private val _state = MutableStateFlow(MarketState(initialStocks(), tick = 0, favorites = emptySet()))
    val state: StateFlow<MarketState> = _state

    suspend fun run() {
        while (true) {
            delay(tickMillis)
            tick()
        }
    }

    fun toggleFavorite(symbol: String) {
        _state.update { state ->
            val favorites = if (symbol in state.favorites) state.favorites - symbol else state.favorites + symbol
            state.copy(favorites = favorites)
        }
    }

    private fun tick() {
        val now = System.currentTimeMillis()
        val current = _state.value
        val changed = List(updatesPerTick) { random.nextInt(current.stocks.size) }.toSet()
        val newPrices = changed.associateWith { index ->
            val stock = current.stocks[index]
            (stock.price * (1 + random.nextDouble(-0.012, 0.012))).coerceAtLeast(1.0)
        }
        val volumes = changed.associateWith { random.nextLong(100, 5_000) }
        _state.value = current.copy(
            tick = current.tick + 1,
            stocks = current.stocks.mapIndexed { index, stock ->
                val newPrice = newPrices[index]
                if (newPrice != null) {
                    stock.copy(
                        price = newPrice,
                        previousPrice = stock.price,
                        history = (stock.history + newPrice).takeLast(historySize),
                        volume = stock.volume + volumes.getValue(index),
                        lastUpdated = now,
                    )
                } else {
                    stock.copy(lastUpdated = now)
                }
            }
        )
    }

    private fun initialStocks(): List<Stock> = List(stockCount) { index ->
        val symbol = symbolFor(index)
        val open = random.nextDouble(10.0, 900.0)
        var price = open
        val history = List(historySize) {
            price = (price * (1 + random.nextDouble(-0.01, 0.01))).coerceAtLeast(1.0)
            price
        }
        Stock(
            symbol = symbol,
            name = "${companyNames[index % companyNames.size]} ${index / companyNames.size + 1}",
            price = price,
            previousPrice = price,
            openPrice = open,
            history = history,
            volume = random.nextLong(10_000, 5_000_000),
            lastUpdated = System.currentTimeMillis(),
        )
    }

    private fun symbolFor(index: Int): String {
        val letters = ('A'..'Z').toList()
        return buildString {
            append(letters[(index * 7 + 3) % 26])
            append(letters[(index * 11 + 5) % 26])
            append(letters[(index * 13 + 1) % 26])
            if (index % 3 == 0) append(letters[(index * 17 + 9) % 26])
        }
    }

    private companion object {
        val companyNames = listOf(
            "Acme", "Globex", "Initech", "Umbrella", "Hooli", "Stark", "Wayne",
            "Wonka", "Cyberdyne", "Soylent", "Tyrell", "Vandelay",
        )
    }
}

private val ScreenBackground = Color(0xFFF2F4F8)
private val CardBackground = Color.White
private val CardBorder = Color(0xFFE2E8F0)
private val TextPrimary = Color(0xFF1B1F2B)
private val TextSecondary = Color(0xFF64748B)
private val UpColor = Color(0xFF16A34A)
private val DownColor = Color(0xFFDC2626)
private val HeaderTop = Color(0xFF1E3A8A)
private val HeaderBottom = Color(0xFF3B82F6)

private const val SHARES_HELD = 25

@Composable
fun MarketDashboardScreen(modifier: Modifier = Modifier) {
    RecompositionCounterEffect("MarketDashboardScreen")
    val simulator = remember { MarketSimulator() }
    LaunchedEffect(simulator) { simulator.run() }
    val state by simulator.state.collectAsState()

    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            now = System.currentTimeMillis()
            delay(16)
        }
    }

    var query by remember { mutableStateOf("") }
    var sortMode by remember { mutableStateOf(SortMode.Change) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val visibleStocks = state.stocks
        .filter { it.symbol.contains(query, ignoreCase = true) || it.name.contains(query, ignoreCase = true) }
        .sortedWith(sortMode.comparator)

    val summary = computeMarketSummary(state.stocks)

    val collapseDistancePx = with(density) { 120.dp.toPx() }
    val collapse = if (listState.firstVisibleItemIndex > 0) 1f
    else (listState.firstVisibleItemScrollOffset / collapseDistancePx).coerceIn(0f, 1f)
    val showScrollToTop = listState.firstVisibleItemIndex > 3

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ScreenBackground)
        ) {
            MarketHeader(
                now = now,
                tick = state.tick,
                collapse = collapse,
            )
            SearchBar(
                query = query,
                onQueryChange = { query = it },
            )
            SortChips(
                selected = sortMode,
                onSelect = { sortMode = it },
            )
            PortfolioSummary(summary)
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(vertical = 8.dp),
                modifier = Modifier
                    .weight(1f)
                    .background(ScreenBackground),
            ) {
                items(visibleStocks) { stock ->
                    StockRow(
                        stock = stock,
                        isFavorite = stock.symbol in state.favorites,
                        onToggleFavorite = { simulator.toggleFavorite(stock.symbol) },
                    )
                }
            }
        }

        if (showScrollToTop) {
            ScrollToTopButton(
                onClick = { scope.launch { listState.animateScrollToItem(0) } },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
            )
        }
    }
}

@Composable
private fun MarketHeader(
    now: Long,
    tick: Long,
    collapse: Float,
    modifier: Modifier = Modifier,
) {
    RecompositionCounterEffect("MarketHeader")
    val timeFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.US)
    val transition = rememberInfiniteTransition(label = "live")
    val pulse by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "livePulse",
    )
    val height: Dp = lerp(150.dp, 72.dp, collapse)
    val titleSize: TextUnit = lerp(34.sp, 22.sp, collapse)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(HeaderTop, HeaderBottom)))
            .statusBarsPadding()
            .height(height)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Markets",
                color = Color.White,
                fontSize = titleSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
            LiveIndicator(pulse = pulse)
        }
        if (collapse < 0.5f) {
            Text(
                text = "Updated ${timeFormat.format(Date(now))} · tick #$tick",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.alpha(1f - collapse * 2),
            )
        }
    }
}

@Composable
private fun LiveIndicator(pulse: Float, modifier: Modifier = Modifier) {
    RecompositionCounterEffect("LiveIndicator")
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .alpha(pulse)
                .background(Color(0xFFFF4D4F), CircleShape)
        )
        Spacer(Modifier.width(6.dp))
        Text("LIVE", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    RecompositionCounterEffect("SearchBar")
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search symbol or company") },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
private fun SortChips(
    selected: SortMode,
    onSelect: (SortMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    RecompositionCounterEffect("SortChips")
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SortMode.entries.forEach { mode ->
            val isSelected = mode == selected
            Text(
                text = mode.label,
                color = if (isSelected) Color.White else TextPrimary,
                fontSize = 13.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (isSelected) HeaderTop else CardBackground)
                    .border(1.dp, CardBorder, RoundedCornerShape(50))
                    .clickable { onSelect(mode) }
                    .padding(horizontal = 14.dp, vertical = 6.dp),
            )
        }
    }
}

@Composable
private fun PortfolioSummary(summary: MarketSummary, modifier: Modifier = Modifier) {
    RecompositionCounterEffect("PortfolioSummary")
    val currency = NumberFormat.getCurrencyInstance(Locale.US)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .background(CardBackground, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        SummaryCell("Portfolio", currency.format(summary.totalValue))
        SummaryCell("Up / Down", "${summary.gainers} / ${summary.losers}")
        SummaryCell("Volatility", "%.2f%%".format(summary.averageVolatility))
        SummaryCell("Pair", summary.mostCorrelated)
    }
}

@Composable
private fun SummaryCell(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(label, color = TextSecondary, fontSize = 11.sp)
        Text(value, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun StockRow(
    stock: Stock,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    RecompositionCounterEffect("StockRow")
    val currency = NumberFormat.getCurrencyInstance(Locale.US)
    val isUp = stock.price >= stock.previousPrice
    val trendColor = if (stock.changeFromOpenPercent >= 0) UpColor else DownColor

    val flash = remember { Animatable(0f) }
    LaunchedEffect(stock.price) {
        flash.snapTo(1f)
        flash.animateTo(0f, tween(600))
    }
    val flashColor = lerp(
        CardBackground,
        if (isUp) UpColor.copy(alpha = 0.3f) else DownColor.copy(alpha = 0.3f),
        flash.value,
    )
    val shape = RoundedCornerShape(14.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .shadow(6.dp, shape)
            .background(CardBackground, shape)
            .background(flashColor, shape)
            .border(1.dp, CardBorder, shape)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = stock.symbol,
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stock.name,
                color = TextSecondary,
                fontSize = 12.sp,
            )
            Text(
                text = "Vol ${formatVolume(stock.volume)}",
                color = TextSecondary,
                fontSize = 11.sp,
            )
        }
        Sparkline(
            history = stock.history,
            color = trendColor,
            modifier = Modifier
                .width(96.dp)
                .height(36.dp),
        )
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .padding(start = 12.dp)
                .width(88.dp),
        ) {
            Text(
                text = currency.format(stock.price),
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
            )
            Text(
                text = "%+.2f%%".format(stock.changeFromOpenPercent),
                color = trendColor,
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
            )
        }
        IconButton(onClick = onToggleFavorite) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                tint = if (isFavorite) Color(0xFFF59E0B) else TextSecondary,
            )
        }
    }
}

@Composable
private fun Sparkline(
    history: List<Double>,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier) {
        val min = history.min()
        val max = history.max()
        val range = (max - min).takeIf { it > 0 } ?: 1.0
        val stepX = size.width / (history.size - 1)
        val line = Path()
        history.forEachIndexed { index, value ->
            val x = index * stepX
            val y = size.height - ((value - min) / range * size.height).toFloat()
            if (index == 0) line.moveTo(x, y) else line.lineTo(x, y)
        }
        val fill = Path().apply {
            addPath(line)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(
            path = fill,
            brush = Brush.verticalGradient(listOf(color.copy(alpha = 0.35f), Color.Transparent)),
        )
        drawPath(path = line, color = color, style = Stroke(width = 2.dp.toPx()))
        val lastY = size.height - ((history.last() - min) / range * size.height).toFloat()
        drawCircle(color = color, radius = 3.dp.toPx(), center = Offset(size.width, lastY))
    }
}

@Composable
private fun ScrollToTopButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    RecompositionCounterEffect("ScrollToTopButton")
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(52.dp)
            .shadow(8.dp, CircleShape)
            .background(HeaderTop, CircleShape)
            .clickable(onClick = onClick),
    ) {
        Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Scroll to top", tint = Color.White)
    }
}

private fun computeMarketSummary(stocks: List<Stock>): MarketSummary {
    val volatilities = stocks.map { stock ->
        val returns = stock.history.zipWithNext { a, b -> (b - a) / a * 100 }
        val mean = returns.average()
        sqrt(returns.sumOf { (it - mean) * (it - mean) } / returns.size)
    }

    var bestPair = ""
    var bestCorrelation = -1.0
    for (i in stocks.indices) {
        for (j in i + 1 until stocks.size) {
            val correlation = correlation(stocks[i].history, stocks[j].history)
            if (abs(correlation) > bestCorrelation) {
                bestCorrelation = abs(correlation)
                bestPair = "${stocks[i].symbol}/${stocks[j].symbol}"
            }
        }
    }

    return MarketSummary(
        totalValue = stocks.sumOf { it.price * SHARES_HELD },
        gainers = stocks.count { it.price > it.openPrice },
        losers = stocks.count { it.price < it.openPrice },
        averageVolatility = volatilities.average(),
        mostCorrelated = bestPair,
    )
}

private fun correlation(a: List<Double>, b: List<Double>): Double {
    val n = minOf(a.size, b.size)
    val meanA = a.takeLast(n).average()
    val meanB = b.takeLast(n).average()
    var covariance = 0.0
    var varianceA = 0.0
    var varianceB = 0.0
    for (k in 0 until n) {
        val da = a[a.size - n + k] - meanA
        val db = b[b.size - n + k] - meanB
        covariance += da * db
        varianceA += da * da
        varianceB += db * db
    }
    return covariance / sqrt(varianceA * varianceB)
}

private fun formatVolume(volume: Long): String = when {
    volume >= 1_000_000 -> "%.1fM".format(volume / 1_000_000.0)
    volume >= 1_000 -> "%.1fK".format(volume / 1_000.0)
    else -> volume.toString()
}

@Preview
@Composable
private fun MarketDashboardScreenPreview() {
    MarketDashboardScreen()
}
