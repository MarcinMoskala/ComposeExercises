package com.marcinmoskala.composeexercises

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

fun loremIpsum(words: Int) = LoremIpsum(words).values.first().replace("\n", " ")