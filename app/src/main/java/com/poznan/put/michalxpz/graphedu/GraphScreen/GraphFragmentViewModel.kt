package com.poznan.put.michalxpz.graphedu.GraphScreen


import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.poznan.put.michalxpz.graphedu.GraphScreen.GraphFragmentContract.*
import com.poznan.put.michalxpz.graphedu.base.BaseViewModel
import com.poznan.put.michalxpz.graphedu.data.Edge
import com.poznan.put.michalxpz.graphedu.data.Graph
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.data.Vertice
import com.poznan.put.michalxpz.graphedu.db.GraphsDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


class GraphFragmentViewModel (private val database: GraphsDatabase, private val graph: Graph, private val navController: NavController) : BaseViewModel<Event, State, Effect>() {


    init {
        viewModelScope.launch {
            val graphAdd = graph
            graphAdd.vertices.add(Vertice(vertex_id = 1, color = "RED", x_pos = 500, y_pos = 500))
            graphAdd.vertices.add(Vertice(vertex_id = 2, color = "RED", x_pos = 700, y_pos = 300))
            graphAdd.vertices.add(Vertice(vertex_id = 3, color = "RED", x_pos = 600, y_pos = 600))
            graphAdd.edges.add(Edge(2, 1))
            graphAdd.edges.add(Edge(3, 2))
            graphAdd.edges.add(Edge(1, 3))
            setState { copy(name, id, graphAdd) }
        }
    }

    override fun createInitialState(): State {
        return State.empty
    }

    override fun handleEvent(event: Event) {
        when(event) {
            Event.OnReturnClick -> { navigateBack() }
            Event.OnCanvasClick -> { onCanvasClick() }
            Event.OnEditEdgesClick -> { onEditEdgeClick() }
            Event.OnEditNodeClick -> { onEditNodeClick() }
            Event.OnNodeDrag -> { onNodeDrag() }
            Event.OnDeleteEdgeClick -> { onDeleteEdgeClick() }
            Event.OnDeleteNodeClick -> { onDeleteNodeClick() }
            Event.OnNodeTap -> { onNodeTap() }
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            navController.popBackStack()
            setEffect { Effect.NavigateBack }
        }
    }

    fun onCanvasClick() {
        viewModelScope.launch {
            setEffect { Effect.CanvasClick }
        }
    }

    fun onNodeDrag() {
        viewModelScope.launch {
            setEffect { Effect.NodeDrag }
        }
    }

    fun onEditEdgeClick() {
        viewModelScope.launch {
            setEffect { Effect.EditEdges }
        }
    }

    fun onEditNodeClick() {
        viewModelScope.launch {
            setEffect { Effect.EditNode }
        }
    }

    fun onDeleteEdgeClick() {
        viewModelScope.launch {
            setEffect { Effect.EditEdges }
        }
    }

    fun onDeleteNodeClick() {
        viewModelScope.launch {
            setEffect { Effect.EditNode }
        }
    }

    fun onNodeTap() {
        viewModelScope.launch {
        }
    }

}