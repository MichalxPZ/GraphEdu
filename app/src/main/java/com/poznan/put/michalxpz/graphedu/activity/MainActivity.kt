package com.poznan.put.michalxpz.graphedu.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.poznan.put.michalxpz.graphedu.drawerMenu.DrawerMenu
import com.poznan.put.michalxpz.graphedu.graphScreen.GraphFragment
import com.poznan.put.michalxpz.graphedu.MainScreen.MainScreen
import com.poznan.put.michalxpz.graphedu.data.Graph
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.db.GraphsDatabase
import com.poznan.put.michalxpz.graphedu.dialogs.AddGraphDialog
import com.poznan.put.michalxpz.graphedu.graphScreen.GraphFragmentViewModel
import com.poznan.put.michalxpz.graphedu.navigation.GraphEduNavigation
import com.poznan.put.michalxpz.graphedu.ui.GraphEduTheme
import com.poznan.put.michalxpz.graphedu.utils.GraphJsonParser
import com.poznan.put.michalxpz.graphedu.utils.NullArgumentException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private var message: String = ""
    private var graphs = mutableListOf<GraphsItem>()
    private var editMessage: String = ""
    private var drawerState = DrawerState(DrawerValue.Closed)
    private var openDialog = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            initObservers()
            installSplashScreen().apply {
                setKeepVisibleCondition {
                    viewModel.uiState.value.isLoading
                }
            }

            GraphEduTheme {
                Surface() {
                    GraphEduApp(viewModel.uiState.collectAsState().value, viewModel)
                }
            }
        }
    }

    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                message = viewModel.uiState.value.message
                graphs = viewModel.uiState.value.graphsItems
                openDialog = viewModel.uiState.value.openDialog
                editMessage = viewModel.uiState.value.editText
                drawerState = viewModel.uiState.value.drawerState
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.effect.collect {
                when(it) {
                    is MainActivityContract.Effect.CloseDialog -> {
                        openDialog = false
                    }

                    is MainActivityContract.Effect.EditText -> {
                        message = editMessage
                    }

                    is MainActivityContract.Effect.FetchingError -> {
                        showToast("Error occurred")
                    }
                    is MainActivityContract.Effect.OpenDialog -> {
                    }
                    is MainActivityContract.Effect.OpenDrawer -> {
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun GraphEduApp(
    state: MainActivityContract.State,
    viewModel: MainActivityViewModel
) {
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()

    Surface() {

        ModalDrawer(
            drawerState = state.drawerState,
            gesturesEnabled = state.drawerState.isOpen,
            drawerContent = {
                DrawerMenu(
                    onDestinationClicked = { route ->
                        viewModel.setEvent(MainActivityContract.Event.OnOpenDrawerButtonClicked)
                        navController.navigate(route) {
                            popUpTo(route = route)
                            launchSingleTop = true
                        }
                    },
                    graphs = state.graphsItems,
                    onButtonClick = {
                        viewModel.setEvent(MainActivityContract.Event.OnCreateButtonClicked)
                    },
                    viewModel = viewModel
                )
            }
        ) {
            GraphEduNavHost(navController = navController, openDrawer = { viewModel.setEvent(MainActivityContract.Event.OnOpenDrawerButtonClicked) }, graphs = state.graphsItems, viewModel = viewModel)
            if (state.openDialog) {
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
                                viewModel.setEvent(MainActivityContract.Event.OnOpenDrawerButtonClicked)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AddGraphDialog(mutableStateOf(state.message), mutableStateOf(state.editText), state.openDialog, viewModel)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun GraphEduNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit,
    graphs: List<GraphsItem>,
    viewModel: MainActivityViewModel
) {
    NavHost(
        navController = navController,
        startDestination = GraphEduNavigation.MainScreen.name,
        modifier = modifier
    ) {
        composable(GraphEduNavigation.MainScreen.name) {
            var grapnNum = 0
            var verticesNum = 0
            var edgesNum = 0
            val graphJsonParser = GraphJsonParser()
            viewModel.database.graphDao.getAllGraphItems().forEach {
                grapnNum += 1
                verticesNum += graphJsonParser.parseJsonStringToGraph(it.graphJson).num_of_vertices
                edgesNum += graphJsonParser.parseJsonStringToGraph(it.graphJson).num_of_edges
                Log.i("STATS", "G$grapnNum v$verticesNum  e$edgesNum")
            }
            MainScreen(navController = navController, graphNum = grapnNum, verticesNum = verticesNum, edgesNum = edgesNum) { openDrawer() }
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
            val graph: GraphsItem = try {
                viewModel.database.graphDao.getAllGraphItems()
                    .filter { it.id == graphId?.toInt() }.get(0)
            } catch (e: IndexOutOfBoundsException) {
                GraphsItem(name = "error", "")
            }

            graphId?.let { GraphFragment(graph, navController) } ?: throw NullArgumentException("graphId")
        }

    }
}

