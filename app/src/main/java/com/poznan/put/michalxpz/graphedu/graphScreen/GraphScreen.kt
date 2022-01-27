package com.poznan.put.michalxpz.graphedu.graphScreen

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.*
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.poznan.put.michalxpz.graphedu.R
import com.poznan.put.michalxpz.graphedu.drawerMenu.TopBar
import com.poznan.put.michalxpz.graphedu.db.GraphsDatabase
import com.poznan.put.michalxpz.graphedu.graphScreen.toolpalette.MultiFabItem
import com.poznan.put.michalxpz.graphedu.graphScreen.toolpalette.MultiFabState
import com.poznan.put.michalxpz.graphedu.graphScreen.toolpalette.MultiFloatingActionButton
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun GraphScreen(
    state: GraphFragmentContract.State,
    title: String,
    onBackArrowClicked: () -> Unit,
    onAddVerticeClicked: () -> Unit,
    onAddEdgeClicked: () -> Unit,
    onVerticePositionChanged: () -> Unit,
    onDeleteVertice: () -> Unit,
    onDeleteEdge: () -> Unit,
    viewModel: GraphFragmentViewModel,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val scope = rememberCoroutineScope()
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                scope.launch {
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
            }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
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
    state.graph.vertices.forEach { vert ->
        _mapOfVertices.put(vert.vertex_id, Pair(vert.x_pos, vert.y_pos))
    }
    val mapOfVertices by remember { mutableStateOf(_mapOfVertices) }
    var selectedVert: Int? by remember { mutableStateOf(null) }

    var toState by remember {
        mutableStateOf(MultiFabState.COLLAPSED)
    }

    val addImage: ImageBitmap = ImageBitmap.imageResource(R.drawable.img)
    val deleteImage: ImageBitmap = ImageBitmap.imageResource(R.drawable.img)
    val items = listOf<MultiFabItem>(
        MultiFabItem(identifier = "add node", icon = addImage, label = "Add vertice", onClick = {onAddVerticeClicked()}),
        MultiFabItem(identifier = "add edge", icon = addImage, label = "Add edge", onClick = {onAddEdgeClicked()}),
        MultiFabItem(identifier = "delete node", icon = deleteImage, label = "Delete vertice", onClick = {onDeleteVertice()}),
        MultiFabItem(identifier = "delete edge", icon = deleteImage, label = "Delete Edge", onClick = {onDeleteEdge()}),
    )

    Scaffold(
        floatingActionButton = {
            MultiFloatingActionButton(items = items, toState = toState, stateChanged = {state -> toState = state}, fabIcon = addImage, showLabels = true, onFabItemClicked = {})
        }
    ) {

        Column() {
            TopBar(
                title = title,
                buttonIcon = Icons.Filled.ArrowBack,
                onButtonClicked = onBackArrowClicked
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
                                onTapGesture(it, selectedVert, mapOfVertices)
                            },
                        )
                        detectDragGestures { change, dragAmount ->
                            change.consumeAllChanges()
                            Log.i("SELECTED", "SELECTED DRAG ID: $selectedVert")
                            if (selectedVert != null) {
                                val x = mapOfVertices.get(selectedVert)!!.first
                                val y = mapOfVertices.get(selectedVert)!!.second
                                Log.i("SELECTED", "SELECTED DRAG: $x, $y")
                                mapOfVertices.put(
                                    selectedVert!!,
                                    Pair(x + dragAmount.x.toInt(), y + dragAmount.y.toInt())
                                )
                                Log.i(
                                    "SELECTED",
                                    "DRAG CHANGED TO: ${x + dragAmount.x}, ${y + dragAmount.y}"
                                )
                            }
                        }
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

                state.graph.edges.forEach {
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


@Preview(name = "graphFragment")
@Composable
fun GraphFragmentPreview() {
    GraphScreen(
//        state = GraphFragmentContract.State.empty,
        title = "Graph Fragment",
        onBackArrowClicked = { },
        state = GraphFragmentContract.State.empty,
        onAddEdgeClicked = {},
        onAddVerticeClicked = {},
        onDeleteEdge = {},
        onDeleteVertice = {},
        onVerticePositionChanged = {},
        viewModel = GraphFragmentViewModel(GraphsDatabase.getDataBase( LocalContext.current),GraphFragmentContract.State.empty.graph, NavController(
            LocalContext.current) )
    )
}