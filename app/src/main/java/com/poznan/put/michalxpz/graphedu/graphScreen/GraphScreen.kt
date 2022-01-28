package com.poznan.put.michalxpz.graphedu.graphScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.poznan.put.michalxpz.graphedu.R
import com.poznan.put.michalxpz.graphedu.data.Edge
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.data.Vertice
import com.poznan.put.michalxpz.graphedu.db.GraphsDatabase
import com.poznan.put.michalxpz.graphedu.drawerMenu.TopBar
import com.poznan.put.michalxpz.graphedu.graphScreen.toolpalette.MultiFabItem
import com.poznan.put.michalxpz.graphedu.graphScreen.toolpalette.MultiFabState
import com.poznan.put.michalxpz.graphedu.graphScreen.toolpalette.MultiFloatingActionButton
import com.poznan.put.michalxpz.graphedu.utils.GraphJsonParser
import kotlinx.coroutines.flow.collect
import java.lang.Math.abs

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun GraphScreen(
    title: String,
    onBackArrowClicked: () -> Unit,
    onAddVerticeClicked: () -> Unit,
    onAddEdgeClicked: () -> Unit,
    onVerticePositionChanged: () -> Unit,
    onDeleteVertice: () -> Unit,
    onDeleteEdge: () -> Unit,
    viewModel: GraphFragmentViewModel,
    graphItem: GraphsItem,
) {

    val state by remember { mutableStateOf(viewModel.uiState.value) }
    val jsonString = viewModel.database.graphDao.getAllGraphItems().filter { it.id == graphItem.id }.get(0)
    val graphJsonParser = GraphJsonParser()
    val graph = graphJsonParser.parseJsonStringToGraph(jsonString.graphJson)

    LaunchedEffect(state) {
        viewModel.uiState.collect {
            state.graph = viewModel.uiState.value.graph
            state.id = viewModel.uiState.value.id
            state.name = viewModel.uiState.value.name
            state.mode = viewModel.uiState.value.mode
        }

        viewModel.effect.collect {
            when (it) {
                is GraphFragmentContract.Effect.CanvasClick -> {
                    println("MODE: ${viewModel.uiState.value.mode}")
                }
                is GraphFragmentContract.Effect.AddEdge -> {
                    println("MODE: ${viewModel.uiState.value.mode}")
                }
                is GraphFragmentContract.Effect.AddNode -> {
                    println("MODE: ${viewModel.uiState.value.mode}")
                }
                is GraphFragmentContract.Effect.FetchingError -> {
                    println("MODE: ${viewModel.uiState.value.mode}")
                }
                is GraphFragmentContract.Effect.NavigateBack -> {
                    println("MODE: ${viewModel.uiState.value.mode}")
                }
                is GraphFragmentContract.Effect.NodeDrag -> {
                    println("MODE: ${viewModel.uiState.value.mode}")
                }
                is GraphFragmentContract.Effect.DeleteEdge -> {
                    println("MODE: ${viewModel.uiState.value.mode}")
                }
                is GraphFragmentContract.Effect.DeleteNode -> {
                    println("MODE: ${viewModel.uiState.value.mode}")
                            }
                        }
                    }
                }

    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val transformableState =
        rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            scale *= zoomChange
            rotation += rotationChange
            offset += offsetChange
        }
    val _mapOfVertices = mutableMapOf<Int, Pair<Int, Int>>()
    graph.vertices.forEach { vert ->
        _mapOfVertices.put(vert.vertex_id, Pair(vert.x_pos, vert.y_pos))
    }

    val mapOfVertices by remember { mutableStateOf(_mapOfVertices) }
    val _edgesList = mutableListOf<Edge>()
    graph.edges.forEach { edge->
        _edgesList.add(edge)
    }
    val edgesList by remember { mutableStateOf(_edgesList) }
    var selectedVert: Int? by remember { mutableStateOf(null) }

    var toState by remember {
        mutableStateOf(MultiFabState.COLLAPSED)
    }

    var mode by remember { mutableStateOf(GraphFragmentContract.StateMode.DEFAULT) }

    val addImage: ImageBitmap = ImageBitmap.imageResource(R.drawable.img)
    val deleteImage: ImageBitmap = ImageBitmap.imageResource(R.drawable.img)
    val items = listOf<MultiFabItem>(
        MultiFabItem(identifier = "add node", icon = addImage, label = "Add vertice"),
        MultiFabItem(identifier = "add edge", icon = addImage, label = "Add edge"),
        MultiFabItem(identifier = "delete node", icon = deleteImage, label = "Delete vertice"),
        MultiFabItem(identifier = "delete edge", icon = deleteImage, label = "Delete Edge"),
    )

    Scaffold(
        floatingActionButton = {
            MultiFloatingActionButton(
                items = items,
                toState = toState,
                stateChanged = { state ->
                    toState = state
                    mode = GraphFragmentContract.StateMode.DEFAULT },
                fabIcon = addImage,
                showLabels = true,
                mode = mode,
                onFabItemClicked = {
                    when(it.identifier) {
                        "add node" -> {
                            mode = GraphFragmentContract.StateMode.ADDNODE
                            onAddVerticeClicked()
                            Log.i("TOOLBAR", it.label)
                        }
                        "add edge" -> {
                            mode = GraphFragmentContract.StateMode.ADDEDGE
                            onAddEdgeClicked()
                            Log.i("TOOLBAR", it.label)
                        }
                        "delete node" -> {
                            mode = GraphFragmentContract.StateMode.DELETENODE
                            onDeleteVertice()
                            Log.i("TOOLBAR", it.label)
                        }
                        "delete edge" -> {
                            mode = GraphFragmentContract.StateMode.DELETEEDGE
                            onDeleteEdge()
                            Log.i("TOOLBAR", it.label)
                        }
                    }
                })
        }
    ) {

        Column() {
            TopBar(
                title = title,
                buttonIcon = Icons.Filled.ArrowBack,
                onButtonClicked = {
                    onBackArrowClicked()
                }
            )
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        rotationZ = rotation,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .transformable(state = transformableState)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                val x = it.x
                                val y = it.y
                                Log.i("SELECTED", "SELECTED: $x, $y")

                                if (selectedVert == null && mode == GraphFragmentContract.StateMode.DEFAULT) {
                                    mapOfVertices.forEach { (id, offset) ->
                                        if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30) {
                                            mode = GraphFragmentContract.StateMode.MOVENODE
                                            selectedVert = id
                                            Log.i("SELECTED", "SELECTED ID: $id")
                                        }
                                    }
                                } else if (mode == GraphFragmentContract.StateMode.MOVENODE) {
                                    Log.i("SELECTED", "SELECTED DRAG: $x, $y")
                                    mode = GraphFragmentContract.StateMode.DEFAULT
                                    mapOfVertices.forEach { (id, offset) ->
                                        if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30) {
                                            selectedVert = id
                                            mode = GraphFragmentContract.StateMode.MOVENODE
                                            Log.i("SELECTED", "SELECTED ID: $id")
                                        }
                                    }
                                    if (mode == GraphFragmentContract.StateMode.DEFAULT) {
                                        mapOfVertices.put(selectedVert!!,
                                            Pair(x.toInt(), y.toInt()))
                                        Log.i(
                                            "SELECTED",
                                            "DRAG CHANGED TO: ${x + mapOfVertices.get(selectedVert)!!.first}, ${
                                                y + mapOfVertices.get(selectedVert)!!.second
                                            }"
                                        )
                                        mapOfVertices.forEach { key, offset ->
                                            graph.vertices.forEach { vaertice ->
                                                if (vaertice.vertex_id == key) {
                                                    vaertice.x_pos = offset.first
                                                    vaertice.y_pos = offset.second
                                                }
                                            }
                                        }
                                        viewModel.updateDatabase(graph)
                                        selectedVert = null
                                    }
                                } else if (mode == GraphFragmentContract.StateMode.ADDNODE) {
                                    mode = GraphFragmentContract.StateMode.DEFAULT
                                    mapOfVertices.forEach { (id, offset) ->
                                        if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30) {
                                            selectedVert = id
                                            mode = GraphFragmentContract.StateMode.ADDNODE
                                            Log.i("SELECTED", "SELECTED ID: $id")
                                        }
                                    }
                                    if (mode == GraphFragmentContract.StateMode.DEFAULT) {
                                        var maxId = 0
                                        mapOfVertices.forEach { vertice ->
                                            if (vertice.key >= maxId) maxId = vertice.key + 1
                                        }
                                        graph.vertices.add(Vertice("RED",
                                            maxId,
                                            x.toInt(),
                                            y.toInt()))
                                        mapOfVertices.put(maxId,
                                            Pair(x.toInt(), y.toInt()))
                                        selectedVert = maxId
                                    }
                                    viewModel.updateDatabase(graph)

                                } else if (mode == GraphFragmentContract.StateMode.ADDEDGE) {
                                    if (selectedVert == null) {
                                        mapOfVertices.forEach { (id, offset) ->
                                            if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30) {
                                                selectedVert = id
                                                mode = GraphFragmentContract.StateMode.ADDEDGE
                                                Log.i("SELECTED", "SELECTED ID: $id")
                                            }
                                        }
                                    } else {
                                        mapOfVertices.forEach { (id, offset) ->
                                            if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30 && id != selectedVert) {
                                                if (!graph.edges.contains(Edge(selectedVert!!,
                                                        id)) && !graph.edges.contains(Edge(id,
                                                        selectedVert!!))
                                                ) {
                                                    edgesList.add(Edge(selectedVert!!, id))
                                                    graph.edges.add(Edge(selectedVert!!, id))
                                                }
                                            }
                                        }
                                        mode = GraphFragmentContract.StateMode.DEFAULT
                                        selectedVert = null
                                    }
                                    viewModel.updateDatabase(graph)

                                } else if (mode == GraphFragmentContract.StateMode.DELETENODE) {
                                    mode = GraphFragmentContract.StateMode.DEFAULT
                                    mapOfVertices.forEach { (id, offset) ->
                                        if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30) {
                                            mode = GraphFragmentContract.StateMode.DEFAULT
                                            selectedVert = id
                                            graph.vertices.forEach {
                                                if (it.vertex_id == selectedVert) {
                                                    graph.vertices.remove(it)
                                                }
                                            }
                                            mapOfVertices.remove(selectedVert)
                                            Log.i("SELECTED", "SELECTED ID: $id")
                                        }
                                    }
                                    viewModel.updateDatabase(graph)

                                } else if (mode == GraphFragmentContract.StateMode.DELETEEDGE) {
                                    if (selectedVert == null) {
                                        mapOfVertices.forEach { (id, offset) ->
                                            if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30) {
                                                selectedVert = id
                                                mode = GraphFragmentContract.StateMode.DELETEEDGE
                                                Log.i("SELECTED", "SELECTED ID: $id")
                                            }
                                        }
                                    } else {
                                        mapOfVertices.forEach { (id, offset) ->
                                            if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30 && id != selectedVert) {
                                                if (graph.edges.contains(Edge(selectedVert!!, id)) || graph.edges.contains(Edge(id, selectedVert!!))) {
                                                    edgesList.remove(Edge(selectedVert!!, id))
                                                    edgesList.remove(Edge(id, selectedVert!!))
                                                    graph.edges.remove(Edge(selectedVert!!, id))
                                                    graph.edges.remove(Edge(id, selectedVert!!))
                                                }
                                            }
                                        }
                                        mode = GraphFragmentContract.StateMode.DEFAULT
                                        selectedVert = null
                                    }
                                    viewModel.updateDatabase(graph)
                                }
                                viewModel.setEvent(GraphFragmentContract.Event.OnCanvasClick(graph, it))
                            },
                        )
                    }
            ) {
                mapOfVertices.forEach { (id, offset) ->
                    var color = Color.Red
                    if (selectedVert == id) {
                        color = Color.Green
                    }
                    drawCircle(
                        color = color,
                        center = Offset(x = offset.first.toFloat(), y = offset.second.toFloat()),
                        radius = size.minDimension / 32
                    )
                }

                edgesList.forEach {
                    val startVertex = it.start
                    val endVertex = it.end

                    mapOfVertices.forEach { (idStart, start) ->
                        mapOfVertices.forEach { (idEnd, end) ->
                            if (idStart == startVertex && idEnd == endVertex) {
                                drawLine(
                                    start = Offset(
                                        x = start.first.toFloat(),
                                        y = start.second.toFloat()
                                    ),
                                    end = Offset(x = end.first.toFloat(), y = end.second.toFloat()),
                                    color = Color.Blue,
                                    strokeWidth = 5F
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Preview(name = "graphFragment")
@Composable
fun GraphFragmentPreview() {
    GraphScreen(
//        state = GraphFragmentContract.State.empty,
        title = "Graph Fragment",
        onBackArrowClicked = { },
        onAddVerticeClicked = {},
        onAddEdgeClicked = {},
        onVerticePositionChanged = {},
        onDeleteVertice = {},
        onDeleteEdge = {},
        viewModel = GraphFragmentViewModel(GraphsDatabase.getDataBase( LocalContext.current),GraphFragmentContract.State.empty.graph, NavController(
            LocalContext.current), 1),
        graphItem = GraphsItem("", "")
    )
}