package com.poznan.put.michalxpz.graphedu.graphScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.db.GraphsDatabase
import com.poznan.put.michalxpz.graphedu.utils.GraphJsonParser

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun GraphFragment(
    graph: GraphsItem,
    navController: NavController,
) {
    val graphJsonParser = GraphJsonParser()
    val graphJson = graphJsonParser.parseJsonStringToGraph(graph.graphJson)
    val viewModel = GraphFragmentViewModel(GraphsDatabase.getDataBase(LocalContext.current), graphJson, navController, graphId = graph.id)
    Scaffold() {
        GraphScreen(
            title = graph.name,
            onBackArrowClicked = { viewModel.setEvent(GraphFragmentContract.Event.OnReturnClick) },
            onAddVerticeClicked = { viewModel.setEvent(GraphFragmentContract.Event.OnAddNodeClick) },
            onAddEdgeClicked = { viewModel.setEvent(GraphFragmentContract.Event.OnAddEdgesClick) },
            onVerticePositionChanged = {},
            onDeleteVertice = { viewModel.setEvent(GraphFragmentContract.Event.OnDeleteNodeClick) },
            onDeleteEdge = { viewModel.setEvent(GraphFragmentContract.Event.OnDeleteEdgeClick) },
            viewModel = viewModel,
            graphItem = graph
        )
    }
}

