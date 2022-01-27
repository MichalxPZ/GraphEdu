package com.poznan.put.michalxpz.graphedu.graphScreen

import android.util.Log
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
    val viewModel = GraphFragmentViewModel(GraphsDatabase.getDataBase(LocalContext.current), graphJson, navController)

    Scaffold() {
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
