package com.poznan.put.michalxpz.graphedu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poznan.put.michalxpz.graphedu.DrawerMenu.DrawerMenu
import com.poznan.put.michalxpz.graphedu.GraphScreen.GraphFragment
import com.poznan.put.michalxpz.graphedu.MainScreen.MainScreen
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.navigation.GraphEduNavigation
import com.poznan.put.michalxpz.graphedu.ui.GraphEduTheme
import com.poznan.put.michalxpz.graphedu.utils.NullArgumentException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            installSplashScreen().apply {
                setKeepVisibleCondition{
                    viewModel.isLoading.value
                }
            }
            GraphEduTheme {
                // A surface container using the 'background' color from the theme
                Surface() {
                    GraphEduApp()
                }
            }
        }
    }
}

@Composable
fun GraphEduApp() {
    val allScreens = GraphEduNavigation.values().toList()
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = GraphEduNavigation.fromRoute(backstackEntry.value?.destination?.route)

    val context = LocalContext.current
    val jsonString = context.resources.openRawResource(R.raw.graphs).bufferedReader().readText()
    val gson = Gson()
    val graphsListType = object : TypeToken<List<GraphsItem>>() {}.type
    val graphs: MutableList<GraphsItem> = gson.fromJson(jsonString, graphsListType)

    Surface() {
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
                    graphs = graphs,
                    onButtonClick = {
                    //TODO implement add graph feature
                        }
                )
            }
        ) {
            GraphEduNavHost(navController = navController, openDrawer = openDrawer, graphs = graphs)
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GraphEduTheme {
        GraphEduApp()
    }
}