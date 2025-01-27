package com.marcinmoskala.composeexercises.ui.modifiers

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import com.marcinmoskala.composeexercises.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

//fun Modifier.clip(shape: Shape) =
//    graphicsLayer(shape = shape, clip = true)

// *** Transparency ***

@Preview
@Composable
fun CustomTransparencyPreview() {
    Image(
        painter = painterResource(id = R.drawable.avatar),
        contentDescription = "Image",
        modifier = Modifier
            .size(100.dp)
            .transparency(0.5f)
    )
}

fun Modifier.transparency(alpha: Float) =
    graphicsLayer { this.alpha = alpha }

// *** Optional Padding ***

@Preview
@Preview(widthDp = 600, heightDp = 400)
@Composable
fun CustomOptionalPaddingPreview() {
    val isLargeScreen = LocalConfiguration.current.screenWidthDp.dp >= 600.dp

    Image(
        painter = painterResource(id = R.drawable.avatar),
        contentDescription = "Image",
        modifier = Modifier
            .size(100.dp)
            .optionalPadding(isLargeScreen, 50.dp)
    )
}

fun Modifier.optionalPadding(enable: Boolean, all: Dp) =
    if (enable) padding(all) else this

// *** Cutout ***

@Preview
@Composable
fun CustomCutoutPreview() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.Blue)
    ) {
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "Image",
            modifier = Modifier
                .size(100.dp)
                .cutoutCircle(x = 70.dp, y = 50.dp, r = 20.dp)
                .cutoutCircle(x = 50.dp, y = 30.dp, r = 5.dp)
                .cutoutCircle(x = 40.dp, y = 55.dp, r = 5.dp)
        )
    }
}

fun Modifier.cutoutCircle(x: Dp, y: Dp, r: Dp) = drawWithContent {
    val nativeCanvas = drawContext.canvas.nativeCanvas
    val checkPoint = nativeCanvas.saveLayer(null, null)
    drawContent()
    drawCircle(
        color = Color.Black, // Color doesn't matter, it will be used as a mask
        radius = r.toPx(),
        center = Offset(x.toPx(), y.toPx()),
        blendMode = BlendMode.Clear
    )
    nativeCanvas.restoreToCount(checkPoint)
}

// *** Gradient Background ***

@Preview
@Composable
fun CustomGradientBackgroundPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .gradientBackground(
                colors = listOf(Color.Blue, Color.Red),
                angle = 45f
            )
    ) {
        Text(
            text = "Gradient Background",
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

fun Modifier.gradientBackground(
    colors: List<Color>,
    angle: Float = 0f,
) = drawBehind {
    val angleRad = angle / 180f * PI
    val x = cos(angleRad).toFloat()
    val y = sin(angleRad).toFloat()

    val radius = sqrt(size.width.pow(2) + size.height.pow(2)) / 2f
    val offset = center + Offset(x * radius, y * radius)

    val exactOffset = Offset(
        x = min(offset.x.coerceAtLeast(0f), size.width),
        y = size.height - min(offset.y.coerceAtLeast(0f), size.height)
    )

    drawRect(
        brush = Brush.linearGradient(
            colors = colors,
            start = Offset(size.width, size.height) - exactOffset,
            end = exactOffset
        ),
        size = size
    )
}

// *** Custom Shadow ***

@Preview
@Composable
fun CustomShadowPreview() {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .customShadow(
                color = Color.Gray,
                borderRadius = 8.dp,
                blurRadius = 4.dp,
                offsetY = 2.dp
            )
            .padding(8.dp)
    ) {
        Text(
            text = "Custom Shadow Example",
            color = Color.White,
        )
    }
}

fun Modifier.customShadow(
    color: Color = Color.Black,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0f.dp,
) = drawBehind {
    drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        val spreadPixel = spread.toPx()
        val leftPixel = (0f - spreadPixel) + offsetX.toPx()
        val topPixel = (0f - spreadPixel) + offsetY.toPx()
        val rightPixel = (size.width + spreadPixel)
        val bottomPixel = (size.height + spreadPixel)

        frameworkPaint.color = color.toArgb()
        frameworkPaint.maskFilter =
            BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL)

        canvas.drawRoundRect(
            left = leftPixel,
            top = topPixel,
            right = rightPixel,
            bottom = bottomPixel,
            radiusX = borderRadius.toPx(),
            radiusY = borderRadius.toPx(),
            paint = paint
        )
    }
}

// *** Fade ***

@Preview
@Composable
fun CustomFadePreview() {
    var enable by remember { mutableStateOf(true) }

    Image(
        painter = painterResource(id = R.drawable.avatar),
        contentDescription = "Image",
        modifier = Modifier
            .size(100.dp)
            .fade(enable)
            .clickable { enable = !enable }
    )
}

@Composable
fun Modifier.fade(enable: Boolean): Modifier {
    val alpha by animateFloatAsState(if (enable) 0.5f else 1.0f)
    return transparency(alpha)
}

// *** Ripple Click ***

@Preview
@Composable
fun NoRippleClickablePreview() {
    Column {
        Text(
            text = "No Ripple Clickable",
            modifier = Modifier
                .noRippleClickable { /* handle click */ }
                .padding(24.dp)
        )
        Text(
            text = "Regular Clickable",
            modifier = Modifier
                .clickable { /* handle click */ }
                .padding(24.dp)
        )
    }
}

@Composable
fun Modifier.noRippleClickable(onClick: () -> Unit) =
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )

// *** Circle Background ***

private class CircleNode(var color: Color) : DrawModifierNode, Modifier.Node() {
    override fun ContentDrawScope.draw() {
        drawCircle(color)
    }
}

private data class CircleElement(val color: Color) : ModifierNodeElement<CircleNode>() {
    override fun create() = CircleNode(color)

    override fun update(node: CircleNode) {
        node.color = color
    }
}

fun Modifier.circleBackground(color: Color) = this then CircleElement(color)

@Preview
@Composable
fun CirclePreview() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .circleBackground(Color.Blue)
    )
}

// *** Percent Padding ***

@Preview
@Composable
fun PaddingPercentPreview() {
    Column {
        Box(
            modifier = Modifier
                .size(100.dp)
                .paddingPercent(0.1f)
                .circleBackground(Color.Blue)
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .paddingPercent(0.5f)
                .circleBackground(Color.Blue)
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .paddingPercent(0.9f)
                .circleBackground(Color.Blue)
        )
    }
}

fun Modifier.paddingPercent(percent: Float) = this then PercentPaddingElement(percent)

data class PercentPaddingElement(
    private val percent: Float,
) : ModifierNodeElement<PercentPaddingNode>() {
    override fun create() = PercentPaddingNode(percent)
    override fun update(node: PercentPaddingNode) {
        node.percent = percent
    }
}

class PercentPaddingNode(var percent: Float) : LayoutModifierNode, Modifier.Node() {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val horizontal = (constraints.maxWidth * percent).toInt()
        val vertical = (constraints.maxHeight * percent).toInt()

        val placeable = measurable.measure(constraints.offset(-horizontal, -vertical))

        val width = constraints.constrainWidth(placeable.width + horizontal)
        val height = constraints.constrainHeight(placeable.height + vertical)
        return layout(width, height) {
            placeable.place(horizontal / 2, vertical / 2)
        }
    }
}
