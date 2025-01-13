package com.marcinmoskala.composeexercises.ui.state

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Preview
@Composable
private fun DerivedState() {

    var state by remember { mutableStateOf("A") }
    var otherState by remember { mutableStateOf("A") }

    val calculatedState = calculate("calculatedState", state)
    val recalculatedState = remember(state) { calculate("recalculatedState", state) }
    val derivedState by remember { derivedStateOf { calculate("derivedState", state) } }

    Column {
        Button(onClick = { state += "A" }) {
            Text("Change state")
        }
        Button(onClick = { otherState += "A" }) {
            Text("Change otherState")
        }
//        Text("State: $state")
//        Text("Derived state: $derivedState")
//        Text("Calculated state: $calculatedState")
//        Text("Recalculated state: $recalculatedState")
//        Text("Other state: $otherState")
    }
}

private fun calculate(name: String, value: String): String {
    println("Calculating $name")
    return value.lowercase()
}

// ***

@Preview
@Composable
private fun ThresholdExample() {
    val threshold = remember { mutableStateOf(0) }

    Column {
        CreateUsernameCalculated(threshold.value)
        CreateUsernameRemembered(threshold.value)
        CreateUsernameDerived(threshold.value)

        Button(onClick = { threshold.value++ }) {
            Text("Increase threshold")
        }
        Button(onClick = { threshold.value-- }) {
            Text("Decrease threshold")
        }
    }
}

@Composable
private fun CreateUsernameCalculated(threshold: Int) {
    val username = remember { mutableStateOf("User") }
    val enabled = username.value.length > threshold
    SideEffect { println("CreateUsernameCalculated recomposed") }
    Column {
        SideEffect { println("CreateUsernameCalculated.Column recomposed") }
        Text("CreateUsernameCalculated")
        UsernameTextField(username)
        CreateButton { enabled }
    }
}

@Composable
private fun CreateUsernameRemembered(threshold: Int) {
    val username = remember { mutableStateOf("User") }
    val enabled = remember(username.value, threshold) { username.value.length > threshold }
    SideEffect { println("CreateUsernameRemembered recomposed") }
    Column {
        SideEffect { println("CreateUsernameRemembered.Column recomposed") }
        Text("CreateUsernameRemembered")
        UsernameTextField(username)
        CreateButton { enabled }
    }
}

@Composable
private fun CreateUsernameDerived(threshold: Int) {
    val username = remember { mutableStateOf("User") }
    val enabled = remember(threshold) { derivedStateOf { username.value.length > threshold } }
    SideEffect { println("CreateUsernameDerived recomposed") }
    Column {
        SideEffect { println("CreateUsernameDerived.Column recomposed") }
        Text("CreateUsernameDerived")
        UsernameTextField(username)
        CreateButton { enabled.value }
    }
}

@Composable
private fun UsernameTextField(username: MutableState<String>) {
    SideEffect { println("UsernameTextField recomposed") }
    TextField(value = username.value, onValueChange = { username.value = it })
}

@Composable
private fun CreateButton(enabled: () -> Boolean) {
    SideEffect { println("CreateButton recomposed") }
    Button(enabled = enabled(), onClick = { /* Create user */ }) {
        Text("Create")
    }
}

// Challenges

// 1
@Composable
@Preview
private fun InvoiceForm() {
    var country by remember { mutableStateOf<Country?>(null) }
    val taxRate = remember { country?.let(::taxRateForCountry) }
    var taxId by remember { mutableStateOf("") } // Should reset it when country changes

    Column {
        Text("Country:")
        Row {
            for (c in Country.entries) {
                Box(
                    contentAlignment = Center,
                    modifier = Modifier
                        .width(70.dp)
                        .clickable { country = c }
                        .padding(4.dp)
                        .border(if (c == country) 4.dp else 1.dp, Color.Black)
                ) {
                    Text(c.name)
                }
            }
        }
        Text("Tax rate: ${taxRate?.times(100)}%")
        TextField(
            value = taxId,
            onValueChange = { taxId = it },
            label = { Text("Tax ID") }
        )
    }
}

enum class Country {
    Poland, Germany, France, UK, USA
}

fun taxRateForCountry(country: Country): Double {
    return when (country) {
        Country.Poland -> 0.23
        Country.Germany -> 0.25
        Country.France -> 0.30
        Country.UK -> 0.15
        Country.USA -> 0.08
    }
}

// 2
@Composable
private fun LazyListWithScrollToTop() {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val showScrollToTopButton = listState.firstVisibleItemIndex > 0

    Column {
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f)
        ) {
            items(100) { index ->
                Text(
                    text = "Item #$index",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }

        if (showScrollToTopButton) {
            Button(
                onClick = {
                    scope.launch {
                        listState.scrollToItem(0)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Scroll to Top")
            }
        }
    }
}

// 3
@Composable
private fun FilteredContactsList(contacts: List<Contact>) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredContacts = contacts.filter { contact ->
        contact.name.contains(searchQuery, ignoreCase = true) ||
                contact.phoneNumber.contains(searchQuery)
    }

    Column {
        // Search TextField
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search contacts") },
            modifier = Modifier.fillMaxWidth()
        )

        // Filtered List
        LazyColumn {
            items(filteredContacts) { contact ->
                ContactItem(contact)
            }
        }
    }
}

@Composable
private fun ContactItem(contact: Contact) {
    ListItem(
        headlineContent = { Text(contact.name) },
        supportingContent = { Text(contact.phoneNumber) }
    )
}

private data class Contact(
    val name: String,
    val phoneNumber: String,
)