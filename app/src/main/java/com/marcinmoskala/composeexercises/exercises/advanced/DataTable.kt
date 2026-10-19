package com.marcinmoskala.composeexercises.exercises.advanced
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun <T> TableState(
    initialData: List<T>,
    initialColumns: Map<String, (T) -> Comparable<*>>,
): TableState<T> = TODO()

@Composable
fun <T> DataTable(
    data: List<T>,
    columns: Map<String, (T) -> Comparable<*>>,
    modifier: Modifier = Modifier,
    headerHeight: Dp = 44.dp,
    rowHeight: Dp = 40.dp,
    swapThreshold: Dp = 40.dp,
) {
    var sortColumnKey by remember { mutableStateOf<String?>(null) }
    var isSortAscending by remember { mutableStateOf(true) }
    val columnOrder: SnapshotStateList<String> = remember(columns) {
        mutableStateListOf<String>().apply { addAll(columns.keys) }
    }

    fun toggleSort(columnKey: String) {
        if (sortColumnKey == columnKey) {
            isSortAscending = !isSortAscending
        } else {
            sortColumnKey = columnKey
            isSortAscending = true
        }
    }

    fun moveColumn(fromIndex: Int, toIndex: Int) {
        if (fromIndex == toIndex) return
        if (fromIndex !in columnOrder.indices) return
        val boundedTo = toIndex.coerceIn(0, columnOrder.lastIndex)
        if (fromIndex == boundedTo) return
        val key = columnOrder.removeAt(fromIndex)
        columnOrder.add(boundedTo, key)
    }

    DataTableUi(
        data = data,
        columns = columns,
        modifier = modifier,
        headerHeight = headerHeight,
        rowHeight = rowHeight,
        swapThreshold = swapThreshold,
        sortColumnKey = sortColumnKey,
        isSortAscending = isSortAscending,
        columnOrder = columnOrder,
        toggleSort = ::toggleSort,
        moveColumn = ::moveColumn
    )
}

@Stable
interface TableState<T> {
    var data: List<T>
    var columns: Map<String, (T) -> Comparable<*>>
    val sortColumnKey: String?
    val isSortAscending: Boolean
    val columnOrder: List<String>

    fun toggleSort(columnKey: String)
    fun setSort(columnKey: String?, ascending: Boolean)
    fun sortByColumn(columnKey: String, ascending: Boolean = true)
    fun clearSort()
    fun moveColumn(fromIndex: Int, toIndex: Int)
}

@Composable
fun <T> rememberTableState(
    data: List<T>,
    columns: Map<String, (T) -> Comparable<*>>,
): TableState<T> {
    val state = remember { TableState(data, columns) }
    LaunchedEffect(data, columns) {
        state.data = data
        state.columns = columns
    }
    return state
}

@Composable
private fun <T> DataTableUi(
    data: List<T>,
    columns: Map<String, (T) -> Comparable<*>>,
    modifier: Modifier = Modifier,
    headerHeight: Dp = 44.dp,
    rowHeight: Dp = 40.dp,
    swapThreshold: Dp = 40.dp,
    sortColumnKey: String?,
    isSortAscending: Boolean,
    columnOrder: List<String>,
    toggleSort: (String) -> Unit,
    moveColumn: (Int, Int) -> Unit,
) {
    Surface(modifier) {
        val orderedColumns = columnOrder.map { key ->
            key to requireNotNull(columns[key]) { "Missing column for key=$key" }
        }

        val comparator = remember(sortColumnKey, isSortAscending, columns) {
            if (sortColumnKey == null) null
            else {
                val selector =
                    requireNotNull(columns[sortColumnKey]) { "Missing selector for $sortColumnKey" }
                Comparator<T> { a, b ->
                    @Suppress("UNCHECKED_CAST")
                    val va = selector(a) as Comparable<Any?>

                    @Suppress("UNCHECKED_CAST")
                    val vb = selector(b) as Comparable<Any?>
                    val base = va.compareTo(vb)
                    if (isSortAscending) base else -base
                }
            }
        }

        val displayed = remember(data, comparator) {
            if (comparator == null) data else data.sortedWith(comparator)
        }

        Column(
            Modifier
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                orderedColumns.forEachIndexed { headerIndex, (key, _) ->
                    val arrow = when {
                        sortColumnKey == key && isSortAscending -> " ▲"
                        sortColumnKey == key && !isSortAscending -> " ▼"
                        else -> ""
                    }
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .headerDragReorder(
                                headerIndex = headerIndex,
                                swapThreshold = swapThreshold,
                                onMove = moveColumn
                            )
                            .padding(horizontal = 8.dp)
                            .clickableNoIndication { toggleSort(key) },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = key + arrow,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(displayed) { item ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(rowHeight)
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        orderedColumns.forEach { (key, selector) ->
                            val value = selector(item)
                            Text(
                                text = value.toString(),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 8.dp),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Modifier.clickableNoIndication(onClick: () -> Unit): Modifier =
    clickable(
        indication = null,
        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    ) { onClick() }

@Composable
private fun Modifier.headerDragReorder(
    headerIndex: Int,
    swapThreshold: Dp,
    onMove: (from: Int, to: Int) -> Unit
): Modifier {
    val thresholdPx =
        with(androidx.compose.ui.platform.LocalDensity.current) { swapThreshold.toPx() }
    // Keep references to the latest values without restarting the gesture on recomposition
    val latestHeaderIndex by rememberUpdatedState(headerIndex)
    val latestOnMove by rememberUpdatedState(onMove)
    return pointerInput(Unit) {
        // Local vars shared across all drag-gesture callbacks via closure
        var accum = 0f
        var dragIndex = 0
        detectDragGesturesAfterLongPress(
            onDragStart = {
                accum = 0f
                dragIndex = latestHeaderIndex   // capture current position at drag start
            },
            onDrag = { change, dragAmount ->
                change.consume()
                accum += dragAmount.x
                // Move one step each time we pass the threshold
                while (accum > thresholdPx) {
                    latestOnMove(dragIndex, dragIndex + 1)
                    dragIndex++
                    accum -= thresholdPx
                }
                while (accum < -thresholdPx) {
                    latestOnMove(dragIndex, dragIndex - 1)
                    dragIndex--
                    accum += thresholdPx
                }
            },
            onDragEnd = { accum = 0f },
            onDragCancel = { accum = 0f }
        )
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
private fun TablePreview() {
    data class Person(val name: String, val age: Int, val city: String)

    val people = remember {
        listOf(
            Person("Alice", 30, "Denver"),
            Person("Bob", 24, "Austin"),
            Person("Carol", 41, "Seattle"),
            Person("Dave", 30, "Boston"),
            Person("Eve", 36, "Chicago")
        )
    }
    val columns = mapOf<String, (Person) -> Comparable<*>>(
        "Name" to { it.name },
        "Age" to { it.age },
        "City" to { it.city }
    )
    Box(modifier = Modifier.safeContentPadding()) {
        DataTable(data = people, columns = columns, modifier = Modifier.padding(16.dp))
    }
}
