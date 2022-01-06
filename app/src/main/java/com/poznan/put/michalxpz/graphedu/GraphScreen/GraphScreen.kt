package com.poznan.put.michalxpz.graphedu.GraphScreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.navigation.NavController
import com.poznan.put.michalxpz.graphedu.DrawerMenu.TopBar
import com.poznan.put.michalxpz.graphedu.data.GraphsItem





@Composable
fun GraphScreen(
    graph: GraphsItem,
    navController: NavController,
    openDrawer: () -> Unit
) {

    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }

    val mapOfX = mutableMapOf<Int, Int>()
    val mapOfY = mutableMapOf<Int,Int>()
    graph.graph.vertices.forEach { graph ->
        mapOfX.put(graph.vertex_id, graph.x_pos)
        mapOfY.put(graph.vertex_id, graph.y_pos)
    }

    var offsetX by remember { mutableStateOf(mapOfX) }
    var offsetY by remember { mutableStateOf(mapOfY) }

    TopBar(
        title = graph.name,
        buttonIcon = Icons.Filled.ArrowBack,
        onButtonClicked = { navController.popBackStack() }
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
            .transformable(state = state)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->

                }
            }
    ) {

        val canvasWidth = size.width
        val canvasHeight = size.height
        graph.graph.vertices.forEach { vert ->
            drawCircle(
                color = Color.Red,
                center = Offset(x = canvasWidth/2 - vert.x_pos.toFloat(), y = canvasHeight/2 - vert.y_pos.toFloat()),
                radius = size.minDimension / 32
            )
        }

        graph.graph.edges.forEach {
            val startVertex = it.start
            val endVertex = it.end

            graph.graph.vertices.forEach { start ->
                graph.graph.vertices.forEach { end ->
                    if (start.vertex_id == startVertex && end.vertex_id == endVertex ) {
                        drawLine(
                            start = Offset(x = canvasWidth/2 - start.x_pos.toFloat(), y = canvasHeight/2 - start.y_pos.toFloat()),
                            end = Offset(x = canvasWidth/2 - end.x_pos.toFloat(), y = canvasHeight/2 - end.y_pos.toFloat()),
                            color = Color.Blue,
                            strokeWidth = 5F
                        )
                    }
                }
            }
        }
    }
}