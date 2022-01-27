package com.poznan.put.michalxpz.graphedu.graphScreen.toolpalette

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.poznan.put.michalxpz.graphedu.R

@Composable
fun MultiFloatingActionButton(
    items: List<MultiFabItem>,
    toState: MultiFabState,
    stateChanged: (fabState: MultiFabState) -> Unit,
    showLabels: Boolean = true
) {
    val transition = updateTransition(targetState = toState, label = "transition")
    val rotation: Float by transition.animateFloat(label = "rotation"){ state ->
        if (state == MultiFabState.EXPANDED) 45f else 0f
    }
    val scale: Float by transition.animateFloat(label="SCALE") { state ->
        if (state == MultiFabState.EXPANDED) 56f else 0f
    }
    val alpha: Float by transition.animateFloat(label = "alpha",
        transitionSpec = { tween(durationMillis = 50)},
        targetValueByState = {state ->
            if (state == MultiFabState.EXPANDED) 1f else 0f
        }
    )


    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        items.forEachIndexed { index, item ->
            MiniFAB(item, alpha, scale, showLabels)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    FloatingActionButton(onClick = {
        stateChanged(
            if (transition.currentState == MultiFabState.EXPANDED) {
                MultiFabState.COLLAPSED
            } else MultiFabState.EXPANDED
        )
    }
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_baseline_add_24), contentDescription = "fab", modifier = Modifier.rotate(rotation))
    }
}