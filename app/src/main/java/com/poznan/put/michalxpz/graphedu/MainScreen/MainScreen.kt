package com.poznan.put.michalxpz.graphedu.MainScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.poznan.put.michalxpz.graphedu.drawerMenu.TopBar

@Composable
fun MainScreen(
    navController: NavController,
    openDrawer: () -> Unit
) {

    val viewModel: MainScreenViewModel = viewModel()
    TopBar(
        title = "GraphEdu",
        buttonIcon = Icons.Filled.Menu,
        onButtonClicked = { openDrawer() }
    )
}