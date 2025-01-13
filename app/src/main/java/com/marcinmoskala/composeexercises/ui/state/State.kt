@file:Suppress("NOTHING_TO_INLINE", "unused", "UNUSED_PARAMETER", "UNUSED_VARIABLE")

package com.marcinmoskala.composeexercises.ui.state

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.marcinmoskala.composeexercises.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import java.math.BigDecimal
import kotlin.random.Random

//@Composable
//fun Toggle() {
//    var checked = false // NO!
//
//    // ...
//}

@Composable
private fun CollectionProcessingChallengeScreen(level: Int) {
    val difficulty = remember(level) { difficultyForLevel(level) }
    val challenge = rememberSaveable(level) { generateChallenge(difficulty) }
}

private fun difficultyForLevel(level: Int): Difficulty {
    return Difficulty(10, listOf("Apple", "Banana"))
}

private data class Difficulty(val steps: Int, val fruits: List<String>)

private data class Challenge(val text: String)

private fun generateChallenge(difficulty: Difficulty): Challenge {
    return Challenge("Challenge")
}

@Composable
private fun Toggle(text: String) {
    var checked = remember { mutableStateOf(false) }

    // ...
}


@Preview
@Composable
private fun StateExamples() {
    Column {
        val state1 = remember { mutableStateOf("") }
        TextField(value = state1.value, onValueChange = { state1.value = it })

        var state2 by remember { mutableStateOf("") }
        TextField(value = state2, onValueChange = { state2 = it })

        val (value3, setValue3) = remember { mutableStateOf("") }
        TextField(value = value3, onValueChange = setValue3)
    }
}

class StateHolder {
    var state = mutableStateOf("")
}

@Preview
@Composable
private fun StateExamples2(holder: StateHolder = remember { StateHolder() }) {
    Column {
        val state1 = holder.state
        TextField(value = state1.value, onValueChange = { state1.value = it })

        var state2 by holder.state
        TextField(value = state2, onValueChange = { state2 = it })

        val (value3, setValue3) = holder.state
        TextField(value = value3, onValueChange = setValue3)
    }
}

// State triggers reader composable recomposition

@Composable
private fun Title(text: String) {
    val offset: State<Int> = animateIntAsState(100, label = "offset")
    // ...
    Column(
        modifier = Modifier
            .offset { IntOffset(x = 0, y = offset.value) }
    ) {
        // ...
    }
}

// Transform objects to a flow

private class ViewModel {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
}

// Narrow the recomposition scope

@Composable
private fun NarrowRecompositionScope(vm: ViewModel) {
//    val isLoading = vm.isLoading.value // NO!
    val isLoading by vm.isLoading.collectAsStateWithLifecycle() // YES!
    // ...
}

@Preview
@Composable
private fun DiceWindow() {
    var number by remember { mutableIntStateOf(Random.nextInt(6)) }
    Column {
        DiceLabel(number = number)
//        DiceLabel(number = { number })
        Text("Add point",
            modifier = Modifier.clickable { number = Random.nextInt(6) }
        )
    }
}

@Composable
private fun DiceLabel(number: Int) {
    Text("Dice result: $number")
}

@Composable
private fun DiceLabel(number: () -> Int) {
    Text("Dice result: ${number()}")
}

private fun dice(): Int = Random.nextInt(6) + 1

// Challenges

// 1
@Composable
private fun NameBadge(profile: Profile) {
    val imageUrl = remember { profile.imageUrl }
    val initials = run {
        val (name, surname) = profile.fullName.split(" ")
        "${name.first()}${surname.first()}"
    }

    if (imageUrl != null) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.avatar),
            modifier = Modifier.size(100.dp)
                .clip(shape = CircleShape)
        )
    } else {
        Box(
            contentAlignment = Center,
            modifier = Modifier.size(100.dp)
                .clip(shape = CircleShape)
                .border(2.dp, Color.Black, CircleShape)
        ) {
            Text(
                text = initials,
                fontSize = 40.sp,
                modifier = Modifier
            )
        }
    }
}

@Preview
@Composable
private fun NameBadgeImagePreview() {
    NameBadge(Profile("Marcin Moskala", "https://lh3.googleusercontent.com/a-/AOh14GgT-nGHTlbxHpiPyUUhgruiheIEyBVEsCDWNdW0xA=s400-c"))
}

@Preview
@Composable
private fun NameBadgeInitialsPreview() {
    NameBadge(Profile("Marcin Moskala", null))
}

private data class Profile(val fullName: String, val imageUrl: String?)

// 2
@Composable
private fun ChallengeScreen(level: Int) {
    val difficulty = difficultyForLevel(level) // Assume it is CPU-intensive
    val steps = difficulty.steps
    val fruits = difficulty.fruits
    var loading = true
    var challenge: Challenge? = remember { null }
    LaunchedEffect(Unit) {
        challenge = generateChallenge(steps, fruits)
        loading = false
    }

    if (loading) {
        Text("Loading...")
    } else {
        Text(challenge!!.text)
    }
}

private fun generateChallenge(steps: Int, fruits: List<String>): Challenge {
    return Challenge("Challenge")
}

// 3
@Composable
private fun ShoppingCartItem(
    initialPrice: Double,
    quantity: Int
) {
    val discountPercentage = remember {
        when {
            initialPrice >= 100.0 -> BigDecimal("20")
            initialPrice >= 50.0 -> BigDecimal("10")
            else -> BigDecimal.ZERO
        }
    }

    val adjustedMaxQuantity = remember {
        when {
            discountPercentage.toDouble() >= 80 -> minOf(quantity, 3)
            discountPercentage.toDouble() >= 40 -> minOf(quantity, 5)
            else -> quantity
        }.toBigDecimal()
    }

    val finalPrice = remember {
        val subtotal = initialPrice.toBigDecimal() * adjustedMaxQuantity
        (subtotal * BigDecimal("100.00") - discountPercentage / BigDecimal("100.00")).setScale(2)
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Discount: $discountPercentage%",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Max Quantity: $adjustedMaxQuantity",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Final Price: $$finalPrice",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
@Preview
private fun ShoppingCartItemPreview() {
    ShoppingCartItem(100.0, 10)
}

// Puzzler

private fun getIntFlow() = flow {
    emit(Random.nextInt())
}

@Composable
private fun dev() {
    val i by getIntFlow().collectAsState(0)
    Text("Hello: $i !")
}