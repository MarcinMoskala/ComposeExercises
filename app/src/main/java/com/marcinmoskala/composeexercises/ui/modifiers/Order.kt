package com.marcinmoskala.composeexercises.ui.modifiers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marcinmoskala.composeexercises.R

@Preview
@Composable
fun Order() {
    Box(
        modifier = Modifier.size(100.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "Image",
            modifier = Modifier
                .padding(20.dp)
                .size(30.dp) // Sets the size of the image
                .border(4.dp, Black) // Adds a border around the image
                .fillMaxSize()
//                .padding(10.dp) // Decreases the size of the image and adds an offset
//                .clip(CircleShape) // Adds a mask to the current size
        )
    }
}
// Hey Márton, I decided to spend some time today on understanding the order of modifiers in Jetpack Compose.
// I read your article, and the proposed mental model, to look at modifiers like they are applied from the bottom to the top,
// but I have a lot of trouble with this approach.

// Consider the following example:
// Box(
//    modifier = Modifier.size(50.dp)
//) {
//    Image(
//        painter = painterResource(id = R.drawable.avatar),
//        contentDescription = "Image",
//        modifier = Modifier
//            .size(30.dp)
//            .padding(10.dp)
//    )
//}
// If we think from the bottom to the top, I would expect image to have size of 30dp.
// But in fact, the image is 10dp, because we first set size to 30dp, and then we add padding of 10dp, which makes the image smaller by 10dp on each side.
// Just as described in here: https://youtu.be/OeC5jMV342A?si=g6cm9c737W31GvLJ&t=647

// If we changed the order of size and padding, the image would be 30dp, because we first set size to 30dp, and then we add padding of 10dp,

// I think that the opposite approach is more correct: the modifiers are applied from the top to the bottom, but what needs to be understood is that padding is not internal to the modifier, but it adds external space and makes element smaller.

// You seem to look at padding as a modifier that adds padding to the element, where in fact it makes content smaller in its bounds.
// Consider this example:
//         Image(
//            painter = painterResource(id = R.drawable.avatar),
//            contentDescription = "Image",
//            modifier = Modifier
//                .size(100.dp)
//                .padding(49.dp)
//        )
// If padding were added, and element would be sided according to the padding, the image would be 100dp + 49dp + 49dp = 198dp wide (or around 50 dp after scaling by size)
// But in fact, the real image is only 2dp, because padding takes 49dp from each side, and the image is 100dp wide. Padding is added, and the content is scaled.
// If you look at the implementation of padding, you will see that it is implemented as a layout modifier. It adds a layout, where the content is scaled and located in the center.
// private class PaddingNode(
//    var start: Dp = 0.dp,
//    var top: Dp = 0.dp,
//    var end: Dp = 0.dp,
//    var bottom: Dp = 0.dp,
//    var rtlAware: Boolean
//) : LayoutModifierNode, Modifier.Node() {
//
//    override fun MeasureScope.measure(
//        measurable: Measurable,
//        constraints: Constraints
//    ): MeasureResult {
//
//        val horizontal = start.roundToPx() + end.roundToPx()
//        val vertical = top.roundToPx() + bottom.roundToPx()
//
//        val placeable = measurable.measure(constraints.offset(-horizontal, -vertical))
//
//        val width = constraints.constrainWidth(placeable.width + horizontal)
//        val height = constraints.constrainHeight(placeable.height + vertical)
//        return layout(width, height) {
//            if (rtlAware) {
//                placeable.placeRelative(start.roundToPx(), top.roundToPx())
//            } else {
//                placeable.place(start.roundToPx(), top.roundToPx())
//            }
//        }
//    }
//}