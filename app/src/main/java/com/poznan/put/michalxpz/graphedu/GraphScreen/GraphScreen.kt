package com.poznan.put.michalxpz.graphedu.GraphScreen

import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.poznan.put.michalxpz.graphedu.data.GraphsItem

@Composable
fun GraphFragment(
    graph: GraphsItem,
    navController: NavController,
    openDrawer: () -> Unit,
    graphs: List<GraphsItem>
) {
//    val viewModel = GraphFragmentViewModel()
//    val scale by remember { mutableStateOf(1f) }
//    val rotation by remember { mutableStateOf(0f) }
//    val offset by remember { mutableStateOf(Offset.Zero) }

//    val mapOfVertices = mutableMapOf<Int, Pair<Int, Int>>()
//    graph.graph.vertices.forEach { vert ->
//        mapOfVertices.put(vert.vertex_id, Pair(vert.x_pos, vert.y_pos))
//    }


//    val state = GraphFragmentContract.State(
//        scale = scale,
//        rotation = rotation,
//        offset = offset,
//        mapOfVerticesPositions = mapOfVertices,
//        edges = graph.graph.edges,
//        selectedVert = null
//    )

    GraphScreen(
//        state = state,
        title = graph.name,
        onBackArrowClicked = { navController.popBackStack() },
        graph = graph,
    )
}