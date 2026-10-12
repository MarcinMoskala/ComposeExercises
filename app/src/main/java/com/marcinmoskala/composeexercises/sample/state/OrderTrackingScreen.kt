@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.marcinmoskala.composeexercises.sample.state

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marcinmoskala.composeexercises.CountAndDisplayRecompositions
import com.marcinmoskala.composeexercises.exercises.advanced.RecompositionCounterEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun OrderTrackingScreen(tracker: OrderTracker, modifier: Modifier = Modifier) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        RecompositionCounterEffect("OrderTrackingScreen")
        val transition = rememberInfiniteTransition()
        val truckOffset by transition.animateFloat(
            initialValue = 0f,
            targetValue = maxWidth.value - 48.dp.value,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        val status = tracker.getDetailedStatus()

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Order #38291",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = status,
                style = MaterialTheme.typography.bodyMedium
            )
            Icon(
                imageVector = TruckIcon,
                contentDescription = "Delivery in progress",
                modifier = Modifier
                    .size(48.dp)
                    .offset(x = truckOffset.dp)
            )
        }
    }
}

interface OrderTracker {
    fun getDetailedStatus(): String
    val detailedStatus: StateFlow<String>
}

@Preview(showBackground = true)
@Composable
private fun OrderTrackingScreenPreview() {
    val tracker: OrderTracker = remember { FakeOrderTracker() }
    CountAndDisplayRecompositions {
        OrderTrackingScreen(tracker)
    }
}

class FakeOrderTracker  : OrderTracker {
    val scope = CoroutineScope(SupervisorJob())

    init {
        scope.launch {
            delay(2000)
            detailedStatus.value = "Package is on its way to the distribution center"
            delay(2000)
            detailedStatus.value = "Package is on its way to your location"
            delay(2000)
            detailedStatus.value = "Package has been delivered"
        }
    }

    override val detailedStatus =
        MutableStateFlow("Package is on its way to the distribution center")

    val itemsToSortToSimulateHeavyOperation by lazy {
        List(10_000_000) { "Item$it".hashCode().toString() }
    }

    override fun getDetailedStatus(): String {
        itemsToSortToSimulateHeavyOperation.sorted()
        return detailedStatus.value
    }
}

private val TruckIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "Truck",
        defaultWidth = 24.0.dp,
        defaultHeight = 24.0.dp,
        viewportWidth = 24.0f,
        viewportHeight = 24.0f
    ).path(
        fill = SolidColor(Color.Black),
        stroke = null,
        strokeLineWidth = 0.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 4.0f,
        pathFillType = PathFillType.NonZero
    ) {
        // Main truck body (cargo area + cab frame)
        moveTo(20f, 8f)
        horizontalLineToRelative(-3f)
        verticalLineTo(4f)
        horizontalLineTo(3f)
        curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
        verticalLineToRelative(11f)
        horizontalLineToRelative(2f)
        curveToRelative(0f, 1.66f, 1.34f, 3f, 3f, 3f)
        reflectiveCurveToRelative(3f, -1.34f, 3f, -3f)
        horizontalLineToRelative(6f)
        curveToRelative(0f, 1.66f, 1.34f, 3f, 3f, 3f)
        reflectiveCurveToRelative(3f, -1.34f, 3f, -3f)
        horizontalLineToRelative(2f)
        verticalLineToRelative(-5f)
        lineToRelative(-3f, -4f)
        close()
        // Rear wheel
        moveTo(6f, 18.5f)
        curveToRelative(-0.83f, 0f, -1.5f, -0.67f, -1.5f, -1.5f)
        reflectiveCurveToRelative(0.67f, -1.5f, 1.5f, -1.5f)
        reflectiveCurveToRelative(1.5f, 0.67f, 1.5f, 1.5f)
        reflectiveCurveToRelative(-0.67f, 1.5f, -1.5f, 1.5f)
        close()
        // Cab window
        moveTo(19.5f, 9.5f)
        lineToRelative(1.96f, 2.5f)
        horizontalLineTo(17f)
        verticalLineTo(9.5f)
        horizontalLineToRelative(2.5f)
        close()
        // Front wheel
        moveTo(18f, 18.5f)
        curveToRelative(-0.83f, 0f, -1.5f, -0.67f, -1.5f, -1.5f)
        reflectiveCurveToRelative(0.67f, -1.5f, 1.5f, -1.5f)
        reflectiveCurveToRelative(1.5f, 0.67f, 1.5f, 1.5f)
        reflectiveCurveToRelative(-0.67f, 1.5f, -1.5f, 1.5f)
        close()
    }.build()
}