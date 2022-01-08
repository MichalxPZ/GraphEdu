package com.poznan.put.michalxpz.graphedu.GraphScreen

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.poznan.put.michalxpz.graphedu.DrawerMenu.TopBar
import com.poznan.put.michalxpz.graphedu.data.Graph
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import kotlin.math.abs

@Composable
fun GraphScreen(
//    state: GraphFragmentContract.State,
    title: String,
    onBackArrowClicked: () -> Unit,
    graph: GraphsItem,
) {


    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val transformableState = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }

    val _mapOfVertices = mutableMapOf<Int, Pair<Int, Int>>()
    graph.graph.vertices.forEach { vert ->
        _mapOfVertices.put(vert.vertex_id, Pair(vert.x_pos, vert.y_pos))
    }
    val mapOfVertices by remember { mutableStateOf(_mapOfVertices) }
    var selectedVert: Int? by remember { mutableStateOf(null) }

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
                            val x = it.x
                            val y = it.y
                            Log.i("SELECTED", "SELECTED: $x, $y")
                            if (selectedVert == null) {
                                mapOfVertices.forEach { (id, offset) ->
                                    if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30) {
                                        selectedVert = id
                                        Log.i("SELECTED", "SELECTED ID: $id")
                                    }
                                }
                            } else {
                                Log.i("SELECTED", "SELECTED DRAG: $x, $y")
                                mapOfVertices.forEach { (id, offset) ->
                                    if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30) {
                                        selectedVert = id
                                        Log.i("SELECTED", "SELECTED ID: $id")
                                    }
                                }
                                mapOfVertices.put(
                                    selectedVert!!,
                                    Pair(x.toInt(), y.toInt())
                                )
                                Log.i(
                                    "SELECTED",
                                    "DRAG CHANGED TO: ${x + mapOfVertices.get(selectedVert)!!.first}, ${
                                        y + mapOfVertices.get(selectedVert)!!.second
                                    }"
                                )
                                selectedVert = null
                            }
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

            graph.graph.edges.forEach {
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


@Preview(name = "graphFragment")
@Composable
fun GraphFragmentPreview() {
    GraphScreen(
//        state = GraphFragmentContract.State.empty,
        title = "Graph Fragment",
        onBackArrowClicked = { },
        graph = GraphsItem(Graph(listOf(), 0, 0, listOf()), 1, "Graph Fragment")

    )
}