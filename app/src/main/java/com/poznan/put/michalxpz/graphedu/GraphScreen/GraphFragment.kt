package com.poznan.put.michalxpz.graphedu.GraphScreen

import android.content.Context
import android.widget.Toast
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.poznan.put.michalxpz.graphedu.data.Graph
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.db.GraphsDatabase
import com.poznan.put.michalxpz.graphedu.utils.GraphJsonParser
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun GraphFragment(
    graph: GraphsItem,
    navController: NavController,
) {
    val graphJsonParser = GraphJsonParser()
    var graphJson = graphJsonParser.parseJsonStringToGraph(graph.graphJson)
    var name = graph.name
    var id = graph.id
    val viewModel =  GraphFragmentViewModel(GraphsDatabase.getDataBase(LocalContext.current), graphJson, navController)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Scaffold() {
        scope.launch {
            viewModel.uiState.collect {
                graphJson = viewModel.uiState.value.graph
                id = viewModel.uiState.value.id
                name = viewModel.uiState.value.name
            }

            viewModel.effect.collect() {
                when(it) {
                    is GraphFragmentContract.Effect.CanvasClick -> {}
                    is GraphFragmentContract.Effect.EditEdges -> {}
                    is GraphFragmentContract.Effect.EditNode -> {}
                    is GraphFragmentContract.Effect.FetchingError -> { showToast("Error occurred", context)}
                    is GraphFragmentContract.Effect.NavigateBack -> {}
                    is GraphFragmentContract.Effect.NodeDrag -> {}
                }
            }
        }

        GraphScreen(
            state = viewModel.uiState.collectAsState().value,
            title = graph.name,
            onBackArrowClicked = { viewModel.setEvent(GraphFragmentContract.Event.OnReturnClick) },
            onAddVerticeClicked = {},
            onAddEdgeClicked = {},
            onVerticePositionChanged = {},
            onDeleteVertice = {}
        ) {}
    }

}


private fun showToast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}