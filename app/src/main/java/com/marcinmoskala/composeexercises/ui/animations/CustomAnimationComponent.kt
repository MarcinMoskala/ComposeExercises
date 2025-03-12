package com.marcinmoskala.composeexercises.ui.animations.custom

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.marcinmoskala.composeexercises.loremIpsum
import com.marcinmoskala.composeexercises.ui.animations.FAQItemState
import com.marcinmoskala.composeexercises.ui.animations.FAQState
import com.marcinmoskala.composeexercises.ui.animations.FrequentlyAskedQuestionsPage
import com.marcinmoskala.composeexercises.ui.view.TextPage
import kotlin.random.Random

@Composable
private fun MyAnimatedVisibility(
    visible: Boolean,
    content: @Composable () -> Unit,
) {
    content()
}

@Composable
private fun MyCrossfade(
    targetState: Int,
    content: @Composable (Int) -> Unit,
) {
    content(targetState)
}

@Composable
private fun MyPagerlike(
    page: Int,
    content: @Composable (Int) -> Unit,
) {
    content(page)
}

@Preview
@Composable
private fun CustomAnimationComponentsDisplay(
    @PreviewParameter(CustomAnimationComponentPreview::class) content: @Composable (Int) -> Unit,
) {
    var number by remember { mutableIntStateOf(0) }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        content(number)
        Button(
            onClick = { number++ },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("Next state")
        }
    }
}

class CustomAnimationComponentPreview : PreviewParameterProvider<@Composable (Int) -> Unit> {
    override val values: Sequence<@Composable (Int) -> Unit> = sequenceOf(
        { AnimatedVisibilityPreview(it % 2 == 0) },
        { MyAnimatedVisibilityPreview(it % 2 == 0) },
        { CrossfadePreview(it) },
        { MyCrossfadePreview(it) },
        { PagerPreview(it) },
        { MyPagerlikePreview(it) },
    )
}

@Composable
private fun AnimatedVisibilityPreview(isVisible: Boolean) {
    AnimatedVisibility(isVisible) {
        SamplePage()
    }
}

@Composable
private fun MyAnimatedVisibilityPreview(isVisible: Boolean) {
    MyAnimatedVisibility(isVisible) {
        SamplePage()
    }
}

@Composable
private fun CrossfadePreview(page: Int) {
    Crossfade(page) { currentPage ->
        SamplePage(currentPage)
    }
}

@Composable
private fun MyCrossfadePreview(page: Int) {
    MyCrossfade(page) { currentPage ->
        SamplePage(currentPage)
    }
}

@Composable
private fun PagerPreview(page: Int) {
    val state = rememberPagerState { page }
    LaunchedEffect(page) {
        state.animateScrollToPage(page)
    }
    HorizontalPager(state) { currentPage ->
        SamplePage(currentPage)
    }
}

@Composable
private fun MyPagerlikePreview(page: Int) {
    MyPagerlike(page) { currentPage ->
        SamplePage(currentPage)
    }
}

@Preview
@Composable
private fun SamplePage(seed: Int = 1234) {
    val random = Random(seed)
    FrequentlyAskedQuestionsPage(
        state = FAQState(
            items = listOf(
                FAQItemState(
                    "Question 1",
                    loremIpsum(random.nextInt(300)),
                    isOpen = random.nextBoolean()
                ),
                FAQItemState(
                    "Question 2",
                    loremIpsum(random.nextInt(300)),
                    isOpen = random.nextBoolean()
                ),
                FAQItemState(
                    "Question 3",
                    loremIpsum(random.nextInt(300)),
                    isOpen = random.nextBoolean()
                ),
                FAQItemState(
                    "Question 4",
                    loremIpsum(random.nextInt(300)),
                    isOpen = random.nextBoolean()
                ),
            )
        ),
        toggleOpen = { }
    )
}

