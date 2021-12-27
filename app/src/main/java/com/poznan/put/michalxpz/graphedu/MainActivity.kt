package com.poznan.put.michalxpz.graphedu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.poznan.put.michalxpz.graphedu.GraphScreen.GraphScreen
import com.poznan.put.michalxpz.graphedu.MainScreen.MainScreen
import com.poznan.put.michalxpz.graphedu.navigation.GraphEduNavigation
import com.poznan.put.michalxpz.graphedu.ui.GraphEduTheme
import com.poznan.put.michalxpz.graphedu.utils.NullArgumentException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GraphEduTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
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
    GraphEduNavHost(
        navController = navController
    )
}

@Composable
fun GraphEduNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = GraphEduNavigation.MainScreen.name,
        modifier = modifier
    ) {
        composable(GraphEduNavigation.MainScreen.name) {
            MainScreen()
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
            graphId?.let { GraphScreen(it) } ?: throw NullArgumentException("graphId")
            GraphScreen(graphId);
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GraphEduTheme {
    }
}