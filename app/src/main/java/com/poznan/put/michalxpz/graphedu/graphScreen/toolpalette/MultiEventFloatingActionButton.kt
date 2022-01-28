package com.poznan.put.michalxpz.graphedu.graphScreen.toolpalette

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.poznan.put.michalxpz.graphedu.Colors
import com.poznan.put.michalxpz.graphedu.graphScreen.GraphFragmentContract

@Composable
fun MultiFloatingActionButton(
    fabIcon: ImageBitmap,
    items: List<MultiFabItem>,
    toState: MultiFabState,
    showLabels: Boolean = true,
    stateChanged: (fabstate: MultiFabState) -> Unit,
    onFabItemClicked: (item: MultiFabItem) -> Unit,
    mode: GraphFragmentContract.StateMode
) {
    val transition: Transition<MultiFabState> = updateTransition(targetState = toState, label = "transition")
    val scale: Float by transition.animateFloat(label = "scale") { state ->
        if (state == MultiFabState.EXPANDED) 56f else 0f
    }
    val alpha: Float by transition.animateFloat(label = "alpha",
        transitionSpec = {
            tween(durationMillis = 50)
        }
    ) { state ->
        if (state == MultiFabState.EXPANDED) 1f else 0f
    }
    val shadow: Dp by transition.animateDp( label = "shadow",
        transitionSpec = {
            tween(durationMillis = 50)
        }
    ) { state ->
        if (state == MultiFabState.EXPANDED) 2.dp else 0.dp
    }
    val rotation: Float by transition.animateFloat(label = "rotation") { state ->
        if (state == MultiFabState.EXPANDED) 45f else 0f
    }
    Column(horizontalAlignment = Alignment.End) {
        items.forEach { item ->
            var choosen = false
            when(item.identifier) {
                "add node" -> {
                    choosen = mode == GraphFragmentContract.StateMode.ADDNODE
                }
                "add edge" -> {
                    choosen = mode == GraphFragmentContract.StateMode.ADDEDGE
                }
                "delete node" -> {
                    choosen = mode == GraphFragmentContract.StateMode.DELETENODE
                }
                "delete edge" -> {
                    choosen = mode == GraphFragmentContract.StateMode.DELETEEDGE
                }
            }
            MiniFAB(item, alpha, shadow, scale, showLabels, onFabItemClicked, choosen)
            Spacer(modifier = Modifier.height(20.dp))
        }
        FloatingActionButton(onClick = {
            stateChanged(
                if (transition.currentState == MultiFabState.EXPANDED) {
                    MultiFabState.COLLAPSED
                } else MultiFabState.EXPANDED
            )
        },
        backgroundColor = Colors.Primary001
        ) {
            Icon(
                bitmap = fabIcon,
                contentDescription = "fab",
                modifier = Modifier.rotate(rotation)
                    .size(48.dp)
            )
        }
    }
}