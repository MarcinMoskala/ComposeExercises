package com.marcinmoskala.composeexercises.ui.animations

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marcinmoskala.composeexercises.ui.view.TextPage

@Preview
@Composable
private fun AnimatedContentPreview() {
    var page by remember { mutableStateOf(Page.TermsOfUse) }

//    when(page) {
//        Page.TermsOfUse -> TermsOfUsePage(
//            onNextPage = { page = Page.PrivacyPolicy }
//        )
//        Page.PrivacyPolicy -> PrivacyPolicyPage(
//            onPreviousPage = { page = Page.TermsOfUse },
//            onNextPage = { page = Page.Agreement }
//        )
//        Page.Agreement -> AgreementPage(
//            onPreviousPage = { page = Page.PrivacyPolicy }
//        )
//    }

    AnimatedContent(page,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        }
    ) { currentPage ->
        when(currentPage) {
            Page.TermsOfUse -> TermsOfUsePage(
                onNextPage = { page = Page.PrivacyPolicy }
            )
            Page.PrivacyPolicy -> PrivacyPolicyPage(
                onPreviousPage = { page = Page.TermsOfUse },
                onNextPage = { page = Page.Agreement }
            )
            Page.Agreement -> AgreementPage(
                onPreviousPage = { page = Page.PrivacyPolicy }
            )
        }
    }

//    Crossfade(page) { currentPage ->
//        when(currentPage) {
//            Page.TermsOfUse -> TermsOfUsePage(
//                onNextPage = { page = Page.PrivacyPolicy }
//            )
//            Page.PrivacyPolicy -> PrivacyPolicyPage(
//                onPreviousPage = { page = Page.TermsOfUse },
//                onNextPage = { page = Page.Agreement }
//            )
//            Page.Agreement -> AgreementPage(
//                onPreviousPage = { page = Page.PrivacyPolicy }
//            )
//        }
//    }
}




enum class Page {
    TermsOfUse, PrivacyPolicy, Agreement
}

@Composable
fun TermsOfUsePage(onNextPage: () -> Unit) {
    TextPage(
        text = "Terms of Use\n\n" + LoremIpsum(100).values.first(),
        onNextPage = { onNextPage() }
    )
}

@Composable
fun PrivacyPolicyPage(onNextPage: () -> Unit, onPreviousPage: () -> Unit) {
    TextPage(
        text = "Privacy Policy\n\n" + LoremIpsum(200).values.first().drop(300),
        onNextPage = { onNextPage() },
        onPreviousPage = { onPreviousPage() }
    )
}

@Composable
fun AgreementPage(onPreviousPage: () -> Unit) {
    TextPage(
        text = "Agreement\n\n" + LoremIpsum(300).values.first().drop(600),
        onPreviousPage = { onPreviousPage() }
    )
}

// ***

@Preview
@Composable
private fun CoolCounterPreview() {
    var number by remember { mutableIntStateOf(0) }
    val offset = 200
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedContent(
            targetState = number,
            transitionSpec = {
                if (targetState > initialState) {
                    slideIn(initialOffset = { IntOffset(0, -offset) }) togetherWith
                            slideOut(targetOffset = { IntOffset(0, offset) })
//                    slideIn(initialOffset = { IntOffset(offset, 0) }) + scaleIn() togetherWith
//                            slideOut(targetOffset = { IntOffset(-offset, 0) }) + scaleOut()
                } else {
                    slideIn(initialOffset = { IntOffset(0, offset) }) togetherWith
                            slideOut(targetOffset = { IntOffset(0, +offset) })
//                    slideIn(initialOffset = { IntOffset(-offset, 0) }) + scaleIn() togetherWith
//                            slideOut(targetOffset = { IntOffset(offset, 0) }) + scaleOut()
                }
            },
            modifier = Modifier.padding(16.dp)
                .fillMaxWidth()
        ) { num ->
            Text(
                text = "$num",
                fontSize = 50.sp,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Button(
            onClick = { number++ }
        ) {
            Text("Increment")
        }
        Button(
            onClick = { number-- }
        ) {
            Text("Decrement")
        }
    }
}