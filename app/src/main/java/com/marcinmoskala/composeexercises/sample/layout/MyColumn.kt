package com.marcinmoskala.composeexercises.sample.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
private fun MyColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables: List<Measurable>, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        layout(constraints.maxWidth, constraints.maxHeight) {
            var yPosition = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = yPosition)
                yPosition += placeable.height
            }
        }
    }
}

@Preview(name = "1. Simple boxes - MyColumn")
@Composable
private fun MyColumnSimpleBoxesPreview() {
    MyColumn(
        modifier = Modifier.background(Blue.copy(alpha = 0.2f))
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Red)
        )
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Green)
        )
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Blue)
        )
    }
}

@Preview(name = "1. Simple boxes - Column")
@Composable
private fun ColumnSimpleBoxesPreview() {
    Column(
        modifier = Modifier.background(Blue.copy(alpha = 0.2f))
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Red)
        )
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Green)
        )
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Blue)
        )
    }
}

@Preview(name = "2. Different widths - MyColumn")
@Composable
private fun MyColumnDifferentWidthsPreview() {
    MyColumn(
        modifier = Modifier.background(Blue.copy(alpha = 0.2f))
    ) {
        Box(
            modifier = Modifier
                .size(width = 50.dp, height = 50.dp)
                .background(Red)
        )
        Box(
            modifier = Modifier
                .size(width = 150.dp, height = 50.dp)
                .background(Green)
        )
        Box(
            modifier = Modifier
                .size(width = 100.dp, height = 50.dp)
                .background(Blue)
        )
    }
}

@Preview(name = "2. Different widths - Column")
@Composable
private fun ColumnDifferentWidthsPreview() {
    Column(
        modifier = Modifier.background(Blue.copy(alpha = 0.2f))
    ) {
        Box(
            modifier = Modifier
                .size(width = 50.dp, height = 50.dp)
                .background(Red)
        )
        Box(
            modifier = Modifier
                .size(width = 150.dp, height = 50.dp)
                .background(Green)
        )
        Box(
            modifier = Modifier
                .size(width = 100.dp, height = 50.dp)
                .background(Blue)
        )
    }
}

@Preview(name = "3. With padding - MyColumn")
@Composable
private fun MyColumnWithPaddingPreview() {
    MyColumn(
        modifier = Modifier
            .padding(20.dp)
            .background(Blue.copy(alpha = 0.2f))
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Red)
        )
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Green)
        )
    }
}

@Preview(name = "3. With padding - Column")
@Composable
private fun ColumnWithPaddingPreview() {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .background(Blue.copy(alpha = 0.2f))
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Red)
        )
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Green)
        )
    }
}

@Preview(name = "4. Empty column - MyColumn")
@Composable
private fun MyColumnEmptyPreview() {
    MyColumn(
        modifier = Modifier.background(Red)
    ) {
        // Empty
    }
}

@Preview(name = "4. Empty column - Column")
@Composable
private fun ColumnEmptyPreview() {
    Column(
        modifier = Modifier.background(Red)
    ) {
        // Empty
    }
}

@Preview(name = "5. Single child - MyColumn")
@Composable
private fun MyColumnSingleChildPreview() {
    MyColumn(
        modifier = Modifier.background(Blue.copy(alpha = 0.2f))
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Red)
        )
    }
}

@Preview(name = "5. Single child - Column")
@Composable
private fun ColumnSingleChildPreview() {
    Column(
        modifier = Modifier.background(Blue.copy(alpha = 0.2f))
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Red)
        )
    }
}

@Preview(name = "6. Too many children - MyColumn")
@Composable
private fun MyColumnTooManyChildrenPreview() {
    MyColumn(
        modifier = Modifier.background(Blue.copy(alpha = 0.2f))
    ) {
        repeat(100) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Red)
            )
        }
    }
}

@Preview(name = "6. Too many children - Column")
@Composable
private fun ColumnTooManyChildrenPreview() {
    Column(
        modifier = Modifier.background(Blue.copy(alpha = 0.2f))
    ) {
        repeat(100) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Red)
            )
        }
    }
}

@Preview(name = "7. Too many children and last that is big - MyColumn")
@Composable
private fun MyColumnTooManyChildrenAndBigOnePreview() {
    MyColumn(
        modifier = Modifier.background(Blue.copy(alpha = 0.2f))
    ) {
        repeat(100) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Red)
            )
        }

        Box(
            modifier = Modifier
                .size(600.dp)
                .background(Red)
        )
    }
}

@Preview(name = "7. Too many children and last that is big - Column")
@Composable
private fun ColumnTooManyChildrenAndBigOnePreview() {
    Column(
        modifier = Modifier.background(Blue.copy(alpha = 0.2f))
    ) {
        repeat(100) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Red)
            )
        }

        Box(
            modifier = Modifier
                .size(600.dp)
                .background(Red)
        )
    }
}