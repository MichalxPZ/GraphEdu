package com.poznan.put.michalxpz.graphedu.GraphScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.poznan.put.michalxpz.graphedu.DrawerMenu.TopBar

@Composable
fun GraphScreen(
    graphId: String,
    navController: NavController,
    openDrawer: () -> Unit
) {
    TopBar(
        title = "Graph",
        buttonIcon = Icons.Filled.ArrowBack,
        onButtonClicked = { navController.popBackStack() }
    )
}