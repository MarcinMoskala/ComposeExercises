package com.marcinmoskala.composeexercises.ui.modifiers

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.marcinmoskala.composeexercises.R

@Composable
private fun GroceryItem(
    name: String,
    quantity: Int,
    isBought: Boolean,
    category: GroceryItemCategory?,
    onToggleClicked: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GroceryItemCheckbox(
                isChecked = isBought,
                onClick = onToggleClicked,
            )
            Text(
                text = name,
                fontSize = 16.sp,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (quantity > 1) {
                Text(
                    text = "$quantity",
                    fontSize = 16.sp,
                )
            }
            val categoryIcon = category?.icon
            if (categoryIcon != null) {
                Icon(
                    painterResource(id = categoryIcon),
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }
    }
}

@Composable
private fun GroceryItemCheckbox(
    isChecked: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val checkedColor: Color = Color.Blue
    val uncheckedColor: Color = Color.White
    val shape: Shape = CircleShape
    val checkboxColor: Color by animateColorAsState(if (isChecked) checkedColor else uncheckedColor)
    Box(
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            tint = uncheckedColor,
        )
    }
}

@Preview
@Composable
fun GroceryItemApplePreview() {
    GroceryItem(
        name = "Apple",
        quantity = 2,
        isBought = false,
        category = GroceryItemCategory.FRUITS,
        onToggleClicked = {},
    )
}

@Preview
@Composable
fun GroceryItemMeatPreview() {
    GroceryItem(
        name = "Chicken",
        quantity = 1,
        isBought = true,
        category = GroceryItemCategory.MEAT,
        onToggleClicked = {},
    )
}

@Preview
@Composable
fun GroceryItemDairyPreview() {
    GroceryItem(
        name = "Milk",
        quantity = 1,
        isBought = false,
        category = GroceryItemCategory.DAIRY,
        onToggleClicked = {},
    )
}

@Preview
@Composable
fun GroceryItemNoCategoryPreview() {
    GroceryItem(
        name = "Bread",
        quantity = 20,
        isBought = true,
        category = null,
        onToggleClicked = {},
    )
}

enum class GroceryItemCategory(@DrawableRes val icon: Int? = null) {
    FRUITS(R.drawable.apple),
    MEAT,
    DAIRY(R.drawable.milk),
}