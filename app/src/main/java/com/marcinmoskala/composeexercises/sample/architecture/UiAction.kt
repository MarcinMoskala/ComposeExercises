@file:Suppress("unused", "UNUSED_PARAMETER")

package com.marcinmoskala.composeexercises.sample.architecture

import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable

class ProfileViewModel {
    // ...
    fun onChangeName(name: String) {
    }
    fun onLogout() {
    }
}

@Composable
fun ProfileScreen(
    uiModel: ProfileUiModel,
    onChangeName: (String) -> Unit,
    onLogout: () -> Unit,
) {
    Button(onClick = onLogout) { /*...*/ }
    TextField(uiModel.name, onChangeName)
}

//class ProfileViewModel {
//    // ...
//    fun onUiAction(uiAction: LoginUiAction) {
//        when (uiAction) {
//            is LoginUiAction.ChangeName -> {
//
//            }
//            LoginUiAction.Logout -> {
//
//            }
//        }
//    }
//}
//
//@Composable
//fun ProfileScreen(
//    uiModel: ProfileUiModel,
//    onUiAction: (LoginUiAction) -> Unit,
//) {
//    Button(onClick = { onUiAction(LoginUiAction.Logout) }) { /*...*/ }
//    TextField(uiModel.name, { onUiAction(LoginUiAction.ChangeName(it)) })
//}
//
//sealed interface LoginUiAction {
//    data class ChangeName(val name: String) : LoginUiAction
//    data object Logout : LoginUiAction
//}

data class ProfileUiModel(
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
)