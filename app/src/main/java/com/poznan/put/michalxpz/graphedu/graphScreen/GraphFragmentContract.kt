package com.poznan.put.michalxpz.graphedu.graphScreen

import androidx.compose.ui.geometry.Offset
import com.poznan.put.michalxpz.graphedu.base.UiEffect
import com.poznan.put.michalxpz.graphedu.base.UiEvent
import com.poznan.put.michalxpz.graphedu.base.UiState
import com.poznan.put.michalxpz.graphedu.data.Graph

interface GraphFragmentContract {
    sealed class Effect: UiEffect {
        object NavigateBack: Effect()
        object CanvasClick : Effect()
        object NodeDrag : Effect()
        object AddEdge : Effect()
        object DeleteEdge : Effect()
        object AddNode : Effect()
        object DeleteNode : Effect()

        data class FetchingError(val error: Throwable) : Effect()
    }

    sealed class Event : UiEvent {
        object OnReturnClick : Event()
        class OnCanvasClick(
            val offset: Offset
        ) : Event()
        class OnNodeDrag(
            val offset: Offset
        ) : Event()
        class OnNodeTap(
            val offset: Offset
        ) : Event()
        object OnAddEdgesClick : Event()
        object OnAddNodeClick : Event()
        object OnDeleteNodeClick : Event()
        object OnDeleteEdgeClick : Event()
    }

    data class State(
        var name: String,
        var id: Int,
        var graph: Graph,
        var mode: StateMode,
        var selectedVert: Int?

    ) : UiState {
        companion object {
            val empty = State("", 0, Graph(0, 0, arrayListOf(), arrayListOf()), StateMode.DEFAULT, null)
        }
    }

    enum class StateMode {
        DEFAULT,
        MOVENODE,
        ADDEDGE,
        ADDNODE,
        DELETENODE,
        DELETEEDGE
    }
}