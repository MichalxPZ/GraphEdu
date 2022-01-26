package com.poznan.put.michalxpz.graphedu.GraphScreen

import androidx.compose.ui.geometry.Offset
import com.poznan.put.michalxpz.graphedu.base.UiEffect
import com.poznan.put.michalxpz.graphedu.base.UiEvent
import com.poznan.put.michalxpz.graphedu.base.UiState
import com.poznan.put.michalxpz.graphedu.data.Edge
import com.poznan.put.michalxpz.graphedu.data.Graph

interface GraphFragmentContract {
    sealed class Effect: UiEffect {
        object NavigateBack: Effect()
        object CanvasClick : Effect()
        object NodeDrag : Effect()
        object EditEdges : Effect()
        object EditNode : Effect()

        data class FetchingError(val error: Throwable) : Effect()
    }

    sealed class Event : UiEvent {
        object OnReturnClick : Event()
        object OnCanvasClick : Event()
        object OnNodeDrag : Event()
        object OnNodeTap : Event()
        object OnEditEdgesClick : Event()
        object OnEditNodeClick : Event()
        object OnDeleteNodeClick : Event()
        object OnDeleteEdgeClick : Event()
    }

    data class State(
        val name: String,
        val id: Int,
        val graph: Graph

    ) : UiState {
        companion object {
            val empty = State("", 0, Graph(0, 0, arrayListOf(), arrayListOf()))
        }
    }
}