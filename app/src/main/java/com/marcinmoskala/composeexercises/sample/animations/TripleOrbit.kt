package com.marcinmoskala.composeexercises.sample.animations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private const val startRotationInner = 90f
private const val startRotationMiddle = 135f

@Preview
@Composable
fun TripleOrbitLoadingAnimation(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            strokeWidth = 1.dp,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationZ = 0f
                }
        )
        CircularProgressIndicator(
            strokeWidth = 1.dp,
            modifier = Modifier
                .fillMaxSize()
                .padding(7.5.dp)
                .graphicsLayer {
                    rotationZ = startRotationMiddle
                }
        )
        CircularProgressIndicator(
            strokeWidth = 1.dp,
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
                .graphicsLayer {
                    rotationZ = startRotationInner
                }
        )
    }
}

@Preview
@Composable
private fun TripleOrbitLoadingAnimationPreview() {
    TripleOrbitLoadingAnimation(
        modifier = Modifier
            .size(100.dp)
    )
}