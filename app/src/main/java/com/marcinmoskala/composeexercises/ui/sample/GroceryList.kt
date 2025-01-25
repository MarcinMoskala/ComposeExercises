package com.marcinmoskala.composeexercises.ui.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marcinmoskala.composeexercises.ui.components.RoundedCornerCheckbox
import com.marcinmoskala.composeexercises.ui.sample.GroceryListUiState.AddEditItemForm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

private class GroceryListViewModel {
    private val _uiState = MutableStateFlow(GroceryListUiState())
    val uiState = _uiState.asStateFlow()

    fun onRemoveItem(id: String) {
        _uiState.update {
            it.copy(
                groceryList = it.groceryList.filter { it.id != id }
            )
        }
    }

    fun onToggleItem(id: String) {
        _uiState.update {
            it.copy(
                groceryList = it.groceryList.map { item ->
                    if (item.id == id) item.copy(isBought = !item.isBought)
                    else item
                }
            )
        }
    }

    fun onOpenAddItem() {
        _uiState.update {
            it.copy(addEditItemForm = AddEditItemForm(
                id = null,
                name = "",
                quantity = 1,
            ))
        }
    }

    fun onOpenEditItem(id: String, name: String, quantity: Int) {
        _uiState.update {
            it.copy(addEditItemForm = AddEditItemForm(
                id = id,
                name = name,
                quantity = quantity,
            ))
        }
    }

    fun onHideAddItem() {
        _uiState.update {
            it.copy(addEditItemForm = null)
        }
    }

    fun onFormSetName(name: String) {
        _uiState.update {
            val form = it.addEditItemForm ?: return@update it
            it.copy(addEditItemForm = form.copy(name = name))
        }
    }

    fun onFormSetQuantity(quantity: Int) {
        _uiState.update {
            val form = it.addEditItemForm ?: return@update it
            it.copy(addEditItemForm = form.copy(quantity = quantity))
        }
    }

    fun onFormAccept() {
        val form = uiState.value.addEditItemForm ?: return
        if (form.id != null) {
            updateItem(form.id, form.name, form.quantity)
        } else {
            addItem(form.name, form.quantity)
        }
    }

    fun addItem(name: String, quantity: Int) {
        _uiState.update {
            it.copy(
                groceryList = it.groceryList + GroceryItem(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    quantity = quantity,
                    isBought = false,
                ),
                addEditItemForm = null,
            )
        }
    }

    fun updateItem(id: String, name: String, quantity: Int) {
        _uiState.update {
            it.copy(
                groceryList = it.groceryList.map { item ->
                    if (item.id == id) item.copy(name = name, quantity = quantity)
                    else item
                },
                addEditItemForm = null,
            )
        }
    }
}

private data class GroceryListUiState(
    val groceryList: List<GroceryItem> = emptyList(),
    val addEditItemForm: AddEditItemForm? = null,
) {
    data class AddEditItemForm(
        val id: String?,
        val name: String,
        val quantity: Int,
    )
}

private data class GroceryItem(
    val id: String,
    val name: String,
    val quantity: Int,
    val isBought: Boolean,
)

@Composable
private fun GroceryList(viewModel: GroceryListViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        floatingActionButton = {
            Card(
                onClick = { viewModel.onOpenAddItem() },
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = SpaceBetween,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Item"
                    )
                }
            }
        },
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(uiState.groceryList) { item ->
                GroceryItem(
                    item = item,
                    onToggleClicked = { viewModel.onToggleItem(item.id) },
                    onRemove = { viewModel.onRemoveItem(item.id) },
                    onEdit = { viewModel.onOpenEditItem(item.id, item.name, item.quantity) },
                )
            }
        }
        uiState.addEditItemForm?.let { addItemForm ->
            AddItemBottomForm(
                addEditItemForm = addItemForm,
                onHide = { viewModel.onHideAddItem() },
                onSetName = { viewModel.onFormSetName(it) },
                onSetQuantity = { viewModel.onFormSetQuantity(it) },
                onAccept = { viewModel.onFormAccept() },
            )
        }
    }
}

@Composable
private fun GroceryItem(
    item: GroceryItem,
    onToggleClicked: () -> Unit = {},
    onRemove: () -> Unit,
    onEdit: () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> onEdit()
                SwipeToDismissBoxValue.EndToStart -> onRemove()
                SwipeToDismissBoxValue.Settled -> {}
            }
            return@rememberSwipeToDismissBoxState false
        },
        // positional threshold of 25%
        positionalThreshold = { it * .25f }
    )

    SwipeToDismissBox(
        state = dismissState,
        modifier = Modifier.fillMaxWidth(),
        backgroundContent = { DismissBackground(dismissState) },
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = SpaceBetween,
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RoundedCornerCheckbox(
                        isChecked = item.isBought,
                        onClick = onToggleClicked,
                        modifier = Modifier
                    )
                    Text(
                        item.name,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (item.quantity > 1) {
                        Text(
                            "${item.quantity}",
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        })
}

@Composable
private fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Color.Yellow
        SwipeToDismissBoxValue.EndToStart -> Color.Red
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            Icons.Default.Edit,
            contentDescription = "Edit"
        )
        Spacer(modifier = Modifier)
        Icon(
            Icons.Default.Delete,
            contentDescription = "Delete"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddItemBottomForm(
    addEditItemForm: AddEditItemForm,
    onHide: () -> Unit,
    onSetName: (String) -> Unit,
    onSetQuantity: (Int) -> Unit,
    onAccept: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = {
            onHide()
        },
        sheetState = sheetState
    ) {
        Column {
            TextField(
                value = addEditItemForm.name,
                onValueChange = onSetName,
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = addEditItemForm.quantity.toString(),
                onValueChange = { onSetQuantity(it.toIntOrNull() ?: 0) },
                label = { Text("Quantity") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = onAccept,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (addEditItemForm.id == null) "Add" else "Update")
            }
        }
    }
}

@Preview
@Composable
private fun GroceryListPreview() {
    val viewModel = GroceryListViewModel()
    viewModel.addItem("Milk", 1)
    viewModel.addItem("Eggs", 12)
    viewModel.addItem("Bread", 2)
    viewModel.addItem("Butter", 1)
    viewModel.addItem("Cheese", 1)
    GroceryList(viewModel)
}

@Preview
@Composable
private fun GroceryListAddItemPreview() {
    val viewModel = GroceryListViewModel()
    viewModel.onOpenAddItem()
    GroceryList(viewModel)
}

@Preview
@Composable
private fun GroceryItemPreview() {
    GroceryItem(
        item = GroceryItem(
            id = "1",
            name = "Milk",
            quantity = 1,
            isBought = false,
        ),
        onRemove = {},
        onEdit = {}
    )
}