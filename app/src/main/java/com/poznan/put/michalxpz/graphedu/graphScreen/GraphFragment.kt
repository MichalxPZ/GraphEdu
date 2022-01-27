package com.poznan.put.michalxpz.graphedu.graphScreen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.db.GraphsDatabase
import com.poznan.put.michalxpz.graphedu.utils.GraphJsonParser
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.abs

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
    var mode = viewModel.uiState.value.mode
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Scaffold() {
        scope.launch {
            viewModel.uiState.collect {
                graphJson = viewModel.uiState.value.graph
                id = viewModel.uiState.value.id
                name = viewModel.uiState.value.name
                mode = viewModel.uiState.value.mode
            }

            viewModel.effect.collect() {
                when(it) {
                    is GraphFragmentContract.Effect.CanvasClick -> {
                        showToast("CANVAS CLICK", context)
                    }
                    is GraphFragmentContract.Effect.AddEdge -> {
                        showToast("add edge", context)
                    }
                    is GraphFragmentContract.Effect.AddNode -> {
                        showToast("add node", context)
                    }
                    is GraphFragmentContract.Effect.FetchingError -> {
                        showToast("Error occurred", context)
                    }
                    is GraphFragmentContract.Effect.NavigateBack -> {

                    }
                    is GraphFragmentContract.Effect.NodeDrag -> {
                        showToast("node drag", context)
                    }
                    is GraphFragmentContract.Effect.DeleteEdge -> {
                        showToast("delete edfe", context)
                    }
                    is GraphFragmentContract.Effect.DeleteNode -> {
                        showToast("delete node", context)
                    }
                }
            }
        }

        GraphScreen(
            state = viewModel.uiState.collectAsState().value,
            title = graph.name,
            onBackArrowClicked = { viewModel.setEvent(GraphFragmentContract.Event.OnReturnClick) },
            onAddVerticeClicked = { viewModel.setEvent(GraphFragmentContract.Event.OnAddNodeClick) },
            onAddEdgeClicked = { viewModel.setEvent(GraphFragmentContract.Event.OnAddEdgesClick) },
            onVerticePositionChanged = {},
            onDeleteVertice = { viewModel.setEvent(GraphFragmentContract.Event.OnDeleteNodeClick) },
            onDeleteEdge = { viewModel.setEvent(GraphFragmentContract.Event.OnDeleteEdgeClick) },
            viewModel = viewModel
        )
    }

}


private fun showToast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

private fun onTapGesture(
    it: Offset,
    selectedVert: Int?,
    mapOfVertices: MutableMap<Int, Pair<Int, Int>>,
) {
    var selectedVert1 = selectedVert
    val x = it.x
    val y = it.y
    Log.i("SELECTED", "SELECTED: $x, $y")
    if (selectedVert1 == null) {
        mapOfVertices.forEach { (id, offset) ->
            if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30) {
                selectedVert1 = id
                Log.i("SELECTED", "SELECTED ID: $id")
            }
        }
    } else {
        Log.i("SELECTED", "SELECTED DRAG: $x, $y")
        mapOfVertices.forEach { (id, offset) ->
            if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30) {
                selectedVert1 = id
                Log.i("SELECTED", "SELECTED ID: $id")
            }
        }
        mapOfVertices.put(
            selectedVert1!!,
            Pair(x.toInt(), y.toInt())
        )
        Log.i(
            "SELECTED",
            "DRAG CHANGED TO: ${x + mapOfVertices.get(selectedVert1)!!.first}, ${
                y + mapOfVertices.get(selectedVert1)!!.second
            }"
        )
        selectedVert1 = null
    }
}