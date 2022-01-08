package com.poznan.put.michalxpz.graphedu.GraphScreen


import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.viewModelScope
import com.poznan.put.michalxpz.graphedu.GraphScreen.GraphFragmentContract.*
import com.poznan.put.michalxpz.graphedu.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GraphFragmentViewModel() : BaseViewModel<Event, State, Effect>() {

    override fun createInitialState(): State {
        return State.empty
    }

    override fun handleEvent(event: Event) {
        when(event) {
            Event.OnReturnClick -> setEffect {
                Effect.NavigateBack
            }
            Event.OnCanvasClick -> setEffect {
                Effect.CanvasClick
            }
            Event.OnEditEdgesClick -> setEffect {
                Effect.EditEdges
            }
            Event.OnEditNodeClick -> setEffect {
                Effect.EditNode
            }
            Event.OnSaveClick -> setEffect {
                Effect.SaveClick
            }
            Event.OnNodeDrag -> setEffect {
                Effect.NodeDrag
            }
        }
    }

    init {
        fetchData()
    }

    private fun fetchData() {
        setState {
            State(
                scale = 1f,
                rotation = 0f,
                offset = Offset.Zero,
                mapOfVerticesPositions = mapOfVerticesPositions,
                edges = edges,
                selectedVert = null,
            )
        }
        viewModelScope.launch {
            event.collect {
                handleEvent(it)
            }
        }
    }
}