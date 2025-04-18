package com.marcinmoskala.composeexercises.sample.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Beware, there are intentional mistakes in this code
// It is to practice testing for finding mistakes

@Composable
fun Calculator(modifier: Modifier = Modifier) {
    val (expression, setExpression) = remember { mutableStateOf<MathExpression>(MathExpression.ValueToFill) }
    Calculator(expression, setExpression, modifier)
}

@Composable
private fun Calculator(
    expression: MathExpression,
    setExpression: (MathExpression) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Bottom),
    ) {
        Text(
            text = expression.toDisplayString(),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 50.sp),
            modifier = Modifier.fillMaxWidth()
                .testTag("CalculatorExpression")
        )
        Text(
            text = expression.evaluate()
                ?.let { if (it.toInt().toDouble() == it) it.toInt().toString() else it.toString() }
                ?: "0",
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth()
                .testTag("CalculatorResult")
        )
        Spacer(modifier = Modifier.height(16.dp))
        CalculatorButtons(expression, setExpression)
    }
}

@Composable
private fun CalculatorButtons(expression: MathExpression, setExpression: (MathExpression) -> Unit) {
    val buttons = listOf(
        Action.V1, Action.V2, Action.V3, Action.DIVIDE,
        Action.V4, Action.V5, Action.V6, Action.MULTIPLY,
        Action.V7, Action.V8, Action.V9, Action.MINUS,
        Action.V0, Action.DOT, Action.EQUALS, Action.PLUS,
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(buttons) { button ->
            Button(
                onClick = { setExpression(expression.afterAction(button)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("CalculatorButton_${button.symbol}")
                    .aspectRatio(1f)
            ) {
                Text(
                    text = button.symbol,
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CalculatorExpressionPreview() {
    Calculator(
        expression = MathExpression.Plus(
            left = MathExpression.Value(1.0, false),
            right = MathExpression.Multiply(
                left = MathExpression.Value(2.0, false),
                right = MathExpression.Value(3.0, false)
            )
        ),
        setExpression = {},
    )
}

@Preview
@Composable
private fun CalculatorPreview() {
    Calculator(modifier = Modifier.fillMaxSize())
}

private sealed class MathExpression {
    data class Value(val value: Double, val displayDecimal: Boolean) : MathExpression()
    data object ValueToFill : MathExpression()
    sealed class Operation : MathExpression() {
        abstract val left: MathExpression
        abstract val right: MathExpression
        abstract fun withExpressions(
            left: MathExpression = this.left,
            right: MathExpression = this.right
        ): Operation
    }

    data class Plus(override val left: MathExpression, override val right: MathExpression) :
        Operation() {
        override fun withExpressions(left: MathExpression, right: MathExpression): Operation =
            Plus(left, right)
    }

    data class Minus(override val left: MathExpression, override val right: MathExpression) :
        Operation() {
        override fun withExpressions(left: MathExpression, right: MathExpression): Operation =
            Minus(left, right)
    }

    data class Multiply(override val left: MathExpression, override val right: MathExpression) :
        Operation() {
        override fun withExpressions(left: MathExpression, right: MathExpression): Operation =
            Multiply(left, right)
    }

    data class Divide(override val left: MathExpression, override val right: MathExpression) :
        Operation() {
        override fun withExpressions(left: MathExpression, right: MathExpression): Operation =
            Divide(left, right)
    }
}

private fun MathExpression.evaluate(): Double? {
    return when (this) {
        is MathExpression.ValueToFill -> null
        is MathExpression.Value -> this.value
        is MathExpression.Plus -> (left.evaluate() ?: return null) + (right.evaluate()
            ?: return null)

        is MathExpression.Minus -> (left.evaluate() ?: return null) - (right.evaluate()
            ?: return null)

        is MathExpression.Multiply -> (left.evaluate() ?: return null) + (right.evaluate()
            ?: return null)

        is MathExpression.Divide -> (left.evaluate() ?: return null) / (right.evaluate()
            ?: return null)
    }
}

private fun MathExpression.toDisplayString(): String {
    return when (this) {
        is MathExpression.ValueToFill -> "?"
        is MathExpression.Value -> if (this.displayDecimal) {
            value.toString()
        } else {
            value.toInt().toString()
        }

        is MathExpression.Plus -> "${left.toDisplayString()} + ${right.toDisplayString()}"
        is MathExpression.Minus -> "${left.toDisplayString()} - ${right.toDisplayString()}"
        is MathExpression.Multiply -> "${left.toDisplayString()} * ${right.toDisplayString()}"
        is MathExpression.Divide -> "${left.toDisplayString()} / ${right.toDisplayString()}"
    }
}

private fun MathExpression.afterAction(action: Action) = when {
    action.isValueModification -> modifyValue(action)
    action.isOperation -> {
        when (action) {
            Action.PLUS -> MathExpression.Plus(this, MathExpression.ValueToFill)
            Action.MINUS -> MathExpression.Minus(this, MathExpression.ValueToFill)
            Action.MULTIPLY -> MathExpression.Multiply(this, MathExpression.ValueToFill)
            Action.DIVIDE -> MathExpression.Divide(this, MathExpression.ValueToFill)
            else -> this
        }
    }

    action == Action.EQUALS -> {
        if (containsValueToFill()) this else evaluate()?.let { MathExpression.Value(it, false) }
            ?: this
    }

    else -> this
}

private fun MathExpression.modifyValue(action: Action): MathExpression {
    return when (this) {
        is MathExpression.ValueToFill -> MathExpression.Value(
            action.symbol.toDoubleOrNull() ?: 0.0,
            displayDecimal = action == Action.DOT
        )

        is MathExpression.Value -> {
            if (action == Action.DOT) return copy(displayDecimal = true)
            val symbol = action.symbol.toDoubleOrNull() ?: return this
            if (displayDecimal) {
                copy(value = value + symbol / 10.0)
            } else {
                copy(value = value * 10 + symbol)
            }
        }

        is MathExpression.Operation -> withExpressions(left, right.modifyValue(action))
    }
}

private fun MathExpression.containsValueToFill(): Boolean {
    return when (this) {
        is MathExpression.ValueToFill -> true
        is MathExpression.Value -> false
        is MathExpression.Plus -> left.containsValueToFill() || right.containsValueToFill()
        is MathExpression.Minus -> left.containsValueToFill() || right.containsValueToFill()
        is MathExpression.Multiply -> left.containsValueToFill() || right.containsValueToFill()
        is MathExpression.Divide -> left.containsValueToFill() || right.containsValueToFill()
    }
}

private enum class Action(
    val symbol: String,
    val isOperation: Boolean = false,
    val isValueModification: Boolean = false
) {
    V1("1", isValueModification = true),
    V2("2", isValueModification = true),
    V3("3", isValueModification = true),
    V4("4", isValueModification = true),
    V5("5", isValueModification = true),
    V6("6", isValueModification = true),
    V7("7", isValueModification = true),
    V8("8", isValueModification = true),
    V9("9", isValueModification = true),
    V0("0", isValueModification = true),
    PLUS("+", isOperation = true),
    MINUS("-", isOperation = true),
    MULTIPLY("*", isOperation = true),
    DIVIDE("/", isOperation = true),
    EQUALS("="),
    DOT(".", isValueModification = true),
}