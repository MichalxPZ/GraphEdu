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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.poznan.put.michalxpz.graphedu.MainScreen.MainScreen
import com.poznan.put.michalxpz.graphedu.data.Graph
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.dialogs.AddGraphDialog
import com.poznan.put.michalxpz.graphedu.drawerMenu.DrawerMenu
import com.poznan.put.michalxpz.graphedu.graphScreen.GraphFragment
import com.poznan.put.michalxpz.graphedu.loginScreen.LoginScreen
import com.poznan.put.michalxpz.graphedu.navigation.GraphEduNavigation
import com.poznan.put.michalxpz.graphedu.ui.GraphEduTheme
import com.poznan.put.michalxpz.graphedu.utils.GraphJsonParser
import com.poznan.put.michalxpz.graphedu.utils.NullArgumentException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private val viewModel: MainActivityViewModel by viewModels()
    private var message: String = ""
    private var graphs = ArrayList<GraphsItem>()
    private var editMessage: String = ""
    private var drawerState = DrawerState(DrawerValue.Closed)
    private var openDialog = false


    override fun onStart() {
        super.onStart()
        auth = Firebase.auth
        currentUser = auth.currentUser
    }
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
                    GraphEduApp(viewModel.uiState.collectAsState().value, viewModel, graphs, currentUser, db)
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
    viewModel: MainActivityViewModel,
    graphs: ArrayList<GraphsItem>,
    currentUser: FirebaseUser?,
    externalFirebaseDb: FirebaseFirestore
) {
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val coroutineScope = rememberCoroutineScope()

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
                    viewModel = viewModel,
                    navigateLogout = {
                        navController.navigate(GraphEduNavigation.LoginScreen.name)
                        coroutineScope.launch {
                            state.drawerState.close()
                        }
                    }
                )
            }
        ) {
            if (currentUser != null) {
                GraphEduNavHost(
                    navController = navController,
                    openDrawer = { viewModel.setEvent(MainActivityContract.Event.OnOpenDrawerButtonClicked) },
                    graphs = state.graphsItems,
                    viewModel = viewModel,
                    startDestination = GraphEduNavigation.GraphScreen.name
                )
            } else {
                GraphEduNavHost(
                    navController = navController,
                    openDrawer = { viewModel.setEvent(MainActivityContract.Event.OnOpenDrawerButtonClicked) },
                    graphs = state.graphsItems,
                    viewModel = viewModel,
                    startDestination = GraphEduNavigation.LoginScreen.name
                )
            }
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
                    AddGraphDialog(remember {

                    mutableStateOf(state.message) }, remember { mutableStateOf(state.editText) }, state.openDialog, graphs, viewModel)
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
    graphs: ArrayList<GraphsItem>,
    viewModel: MainActivityViewModel,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(GraphEduNavigation.LoginScreen.name) {
            LoginScreen(navController)
        }

        composable(GraphEduNavigation.MainScreen.name) {
            var grapnNum = 0
            var verticesNum = 0
            var edgesNum = 0
            val graphJsonParser = GraphJsonParser()
            viewModel.database.graphDao.getAllGraphItems(Firebase.auth.currentUser?.uid ?: "0").forEach {
                grapnNum += 1
                verticesNum += graphJsonParser.parseJsonStringToGraph(it.graphJson).num_of_vertices
                edgesNum += graphJsonParser.parseJsonStringToGraph(it.graphJson).num_of_edges
                Log.i("STATS", "G$grapnNum v$verticesNum  e$edgesNum")
            }
            MainScreen(
                navController = navController,
                graphNum = grapnNum,
                verticesNum = verticesNum,
                edgesNum = edgesNum)
            {
                openDrawer()
                graphs.clear()
                viewModel.database.graphDao.getAllGraphItems(Firebase.auth.currentUser?.uid ?: "0").forEach {
                    graphs.add(it)
                }
            }
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
            val graphJsonParser = GraphJsonParser()
            var graphInstance = Graph(0, 0, arrayListOf(), arrayListOf())
            val graph: GraphsItem = try {
                viewModel.database.graphDao.getAllGraphItems(Firebase.auth.currentUser?.uid ?: "0")
                    .filter { it.id == graphId?.toInt() }[0]
            } catch (e: IndexOutOfBoundsException) {
                graphInstance = Graph(0, 0, arrayListOf(), arrayListOf())

                GraphsItem(uid = Firebase.auth.currentUser?.uid ?: "0",name = "name", graphJson = graphJsonParser.parseGraphToJsonString(graphInstance))
            }
            Log.i("OPEN", graph.graphJson)
            graphId?.let { GraphFragment(graph, navController) } ?: throw NullArgumentException("graphId")
        }

    }
}

