package com.marcinmoskala.composeexercises.ui.recomposition

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.plus
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ChatViewModel {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState
    fun onSend() {
        _uiState.update {
            if (it.isMessageEditing != null) {
                it.copy(
                    messages = it.messages.map { message ->
                        if (message.id == it.isMessageEditing) message.copy(text = it.messageInput)
                        else message
                    },
                    messageInput = "",
                    isMessageEditing = null,
                )
            } else {
                it.copy(
                    messages = it.messages + Message(
                        id = it.messages.size,
                        text = it.messageInput
                    ),
                    messageInput = "",
                )
            }
        }
    }

    fun onInputChanged(text: String) {
        _uiState.update { it.copy(messageInput = text) }
    }

    fun onEdit(id: Int) {
        _uiState.update {
            it.copy(
                messageInput = it.messages.first { it.id == id }.text,
                isMessageEditing = id
            )
        }
    }
}

data class UiState(
    val messages: List<Message> = listOf(),
    val messageInput: String = "",
    val isMessageEditing: Int? = null,
)

data class Message(
    val id: Int,
    val text: String,
)

@Composable
fun ChatScreen(vm: ChatViewModel) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    println("ChatScreen recomposition")
    Scaffold(
        topBar = {
            println("Scaffold topBar recomposition")
            ChatTopBar(uiState.messages)
        },
        bottomBar = {
            println("Scaffold bottomBar recomposition")
            ChatBottomBar(uiState.messageInput, vm::onInputChanged, vm::onSend)
        },
    ) { paddingValues ->
        println("Scaffold content recomposition")
        Chat(uiState.messages, vm::onEdit, paddingValues)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(messages: List<Message>) {
    println("ChatTopBar recomposition")
    TopAppBar(title = {
        println("TopAppBar title recomposition")
        Text("Messages ${messages.size}")
    })
}

@Composable
fun ChatBottomBar(messageInput: String, onInputChanged: (String) -> Unit, onSend: () -> Unit) {
    println("ChatBottomBar recomposition")
    Row(modifier = Modifier.fillMaxWidth()) {
        println("Row recomposition")
        TextField(
            value = messageInput,
            onValueChange = onInputChanged,
            modifier = Modifier
                .padding(start = 12.dp, top = 12.dp)
                .weight(1f),
        )
        Button(
            onClick = onSend,
            modifier = Modifier.padding(12.dp)
        ) { Text("Send") }
    }
}

@Composable
fun Chat(messages: List<Message>, onEdit: (Int) -> Unit, paddingValues: PaddingValues) {
    println("Chat recomposition")
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        println("LazyColumn recomposition")
        items(messages) { message ->
            MessageItem(message, onEdit)
        }
    }
}

@Composable
fun MessageItem(message: Message, onEdit: (Int) -> Unit) {
    println("MessageItem (message ${message.id}) recomposition")
    Card(
        modifier = Modifier
            .padding(12.dp)
            .clickable { onEdit(message.id) },
    ) {
        Text(message.text, modifier = Modifier.padding(12.dp))
    }
}

@Preview
@Composable
fun ChatScreenPreview() {
    val vm = remember { ChatViewModel() }
    LaunchedEffect(Unit) {
        vm.onInputChanged("Hello")
        vm.onSend()
        vm.onInputChanged("World")
        vm.onSend()
    }
    ChatScreen(vm)
}

// Optimized version

//class ChatViewModel {
//    val messages = mutableStateOf(persistentListOf<Message>())
//    val messageInput = mutableStateOf("")
//    val messagesCount = derivedStateOf { messages.value.size }
//    private val isMessageEditing = mutableStateOf<Int?>(null)
//
//    fun onSend() {
//        messages.value = if (isMessageEditing.value != null) {
//            messages.value.map { message ->
//                if (message.id == isMessageEditing.value) message.copy(text = messageInput.value)
//                else message
//            }.toPersistentList()
//        } else {
//            messages.value + Message(
//                id = messages.value.size,
//                text = messageInput.value
//            )
//        }
//        messageInput.value = ""
//        isMessageEditing.value = null
//    }
//
//    fun onInputChanged(text: String) {
//        messageInput.value = text
//    }
//
//    fun onEdit(id: Int) {
//        messageInput.value = messages.value.first { it.id == id }.text
//        isMessageEditing.value = id
//    }
//}
//
//data class Message(
//    val id: Int,
//    val text: String,
//)
//
//@Composable
//fun ChatScreen(vm: ChatViewModel) {
//    println("ChatScreen recomposition")
//    Scaffold(
//        topBar = {
//            println("Scaffold topBar recomposition")
//            ChatTopBar({ vm.messagesCount.value })
//        },
//        bottomBar = {
//            println("Scaffold bottomBar recomposition")
//            ChatBottomBar({ vm.messageInput.value }, vm::onInputChanged, vm::onSend)
//        },
//    ) { paddingValues ->
//        println("Scaffold content recomposition")
//        Chat({ vm.messages.value }, vm::onEdit, paddingValues)
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ChatTopBar(messagesCount: () -> Int) {
//    println("ChatTopBar recomposition")
//    TopAppBar(title = {
//        println("TopAppBar title recomposition")
//        Text("Messages ${messagesCount()}")
//    })
//}
//
//@Composable
//fun ChatBottomBar(
//    messageInput: () -> String,
//    onInputChanged: (String) -> Unit,
//    onSend: () -> Unit,
//) {
//    println("ChatBottomBar recomposition")
//    Row(modifier = Modifier.fillMaxWidth()) {
//        println("Row recomposition")
//        TextField(
//            value = messageInput(),
//            onValueChange = onInputChanged,
//            modifier = Modifier
//                .padding(start = 12.dp, top = 12.dp)
//                .weight(1f),
//        )
//        Button(
//            onClick = onSend,
//            modifier = Modifier.padding(12.dp)
//        ) { Text("Send") }
//    }
//}
//
//@Composable
//fun Chat(
//    messages: () -> PersistentList<Message>,
//    onEdit: (Int) -> Unit,
//    paddingValues: PaddingValues,
//) {
//    println("Chat recomposition")
//    LazyColumn(
//        modifier = Modifier
//            .padding(paddingValues)
//            .fillMaxWidth()
//            .fillMaxHeight()
//    ) {
//        items(messages()) { message ->
//            MessageItem(message, onEdit)
//        }
//    }
//}
//
//@Composable
//fun MessageItem(message: Message, onEdit: (Int) -> Unit) {
//    println("MessageItem (message ${message.id}) recomposition")
//    Card(
//        modifier = Modifier
//            .padding(12.dp)
//            .clickable { onEdit(message.id) },
//    ) {
//        Text(message.text, modifier = Modifier.padding(12.dp))
//    }
//}
//
//@Preview
//@Composable
//fun ChatScreenPreview() {
//    val vm = remember { ChatViewModel() }
//    LaunchedEffect(Unit) {
//        vm.onInputChanged("Hello")
//        vm.onSend()
//        vm.onInputChanged("World")
//        vm.onSend()
//    }
//    ChatScreen(vm)
//}