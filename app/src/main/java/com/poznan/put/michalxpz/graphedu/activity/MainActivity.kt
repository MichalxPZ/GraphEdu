package com.poznan.put.michalxpz.graphedu.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.poznan.put.michalxpz.graphedu.DrawerMenu.DrawerMenu
import com.poznan.put.michalxpz.graphedu.GraphScreen.GraphFragment
import com.poznan.put.michalxpz.graphedu.MainScreen.MainScreen
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.dialogs.AddGraphDialog
import com.poznan.put.michalxpz.graphedu.navigation.GraphEduNavigation
import com.poznan.put.michalxpz.graphedu.ui.GraphEduTheme
import com.poznan.put.michalxpz.graphedu.utils.NullArgumentException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            installSplashScreen().apply {
                setKeepVisibleCondition{
                    viewModel.uiState.value.isLoading
                }
            }
            GraphEduTheme {
                Surface() {
                    GraphEduApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun GraphEduApp(viewModel: MainActivityViewModel) {
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()

    Surface() {
        val message = remember { mutableStateOf("Edit Me") }
        val graphs = remember {
            mutableStateOf(viewModel.currentState.graphsItems)
        }

        val openDialog = remember { mutableStateOf(false) }
        val editMessage = remember { mutableStateOf("") }
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val openDrawer = {
            scope.launch {
                drawerState.open()
            }
        }
        ModalDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {
                DrawerMenu(
                    onDestinationClicked = { route ->
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(route) {
                            popUpTo(route = route)
                            launchSingleTop = true
                        }
                    },
                    graphs = graphs.value,
                    onButtonClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        editMessage.value = message.value
                        openDialog.value = true
                    }
                )
            }
        ) {
            GraphEduNavHost(navController = navController, openDrawer = openDrawer, graphs = graphs.value)
            if (openDialog.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = contentColorFor(MaterialTheme.colors.background)
                                .copy(alpha = 0.6f)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                openDialog.value = false
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AddGraphDialog(message, openDialog, editMessage, viewModel)
                }
            }
        }
    }
}

@Composable
fun GraphEduNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    openDrawer: () -> Job,
    graphs: List<GraphsItem>

) {
    NavHost(
        navController = navController,
        startDestination = GraphEduNavigation.MainScreen.name,
        modifier = modifier
    ) {
        composable(GraphEduNavigation.MainScreen.name) {
            MainScreen(navController = navController, openDrawer = { openDrawer() })
        }
        composable(
            route = "${GraphEduNavigation.GraphScreen.name}/{graphId}",
            arguments = listOf(
                navArgument("graphId") {
                    type = NavType.StringType
                }
            )
        ) { entry ->

            val graphId = entry.arguments?.getString("graphId")

            val graph = graphs.filter {
                it.id == graphId?.toInt()
            }.get(0)

            graphId?.let { GraphFragment(graph, navController, { openDrawer() }, graphs) } ?: throw NullArgumentException("graphId")
        }

    }
}