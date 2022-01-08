package com.poznan.put.michalxpz.graphedu.GraphScreen

import androidx.compose.ui.geometry.Offset
import com.poznan.put.michalxpz.graphedu.base.UiEffect
import com.poznan.put.michalxpz.graphedu.base.UiEvent
import com.poznan.put.michalxpz.graphedu.base.UiState
import com.poznan.put.michalxpz.graphedu.data.Edge

interface GraphFragmentContract {
    sealed class Effect: UiEffect {
        object NavigateBack: Effect()
        object CanvasClick : Effect()
        object NodeDrag : Effect()
        object SaveClick : Effect()
        object EditEdges : Effect()
        object EditNode : Effect()

        data class FetchingError(val error: Throwable) : Effect()
    }

    sealed class Event : UiEvent {
        object OnReturnClick : Event()
        object OnCanvasClick : Event()
        object OnNodeDrag : Event()
        object OnSaveClick : Event()
        object OnEditEdgesClick : Event()
        object OnEditNodeClick : Event()
    }

    data class State(
        var scale: Float,
        var rotation: Float,
        var offset: Offset,
        val mapOfVerticesPositions: MutableMap<Int, Pair<Int, Int>>,
        val edges: List<Edge>,
        var selectedVert: Int?

    ) : UiState {
        companion object {
            val empty = State(1f, 0f, Offset.Zero, mutableMapOf(), listOf(), null,)
        }
    }
}