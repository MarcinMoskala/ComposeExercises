package com.marcinmoskala.composeexercises.sample.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Routes
const val ListScreenRoute = "list_screen"
const val DetailScreenRoute = "detail_screen"

@Preview
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ListScreenRoute) {
        composable(ListScreenRoute) {
            ListScreen(navController = navController)
        }
        composable(DetailScreenRoute) {
            DetailScreen(navController = navController)
        }
    }
}

@Composable
fun ListScreen(navController: NavHostController) {
    val listState = rememberLazyListState() //Key Piece!
    val itemCount = 100 //Large size to illustrate scrolling.
    Column {
        Button(onClick = {
            navController.navigate(DetailScreenRoute) {
                popUpTo(ListScreenRoute) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }) {
            Text("Go to Detail Screen")
        }

        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
            items(itemCount) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = "Item #$index", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun DetailScreen(navController: NavHostController) {
    Column {
        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Back to List Screen")
        }
        Text("Detail Screen Content")
    }
}

