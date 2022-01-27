package com.poznan.put.michalxpz.graphedu.graphScreen


import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.poznan.put.michalxpz.graphedu.graphScreen.GraphFragmentContract.*
import com.poznan.put.michalxpz.graphedu.base.BaseViewModel
import com.poznan.put.michalxpz.graphedu.data.Edge
import com.poznan.put.michalxpz.graphedu.data.Graph
import com.poznan.put.michalxpz.graphedu.data.Vertice
import com.poznan.put.michalxpz.graphedu.db.GraphsDatabase
import kotlinx.coroutines.launch


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
            is Event.OnReturnClick -> { navigateBack() }
            is Event.OnCanvasClick -> { onCanvasClick() }
            is Event.OnAddEdgesClick -> { onAddEdgeClick() }
            is Event.OnAddNodeClick -> { onAddNodeClick() }
            is Event.OnNodeDrag -> { onNodeDrag() }
            is Event.OnDeleteEdgeClick -> { onDeleteEdgeClick() }
            is Event.OnDeleteNodeClick -> { onDeleteNodeClick() }
            is Event.OnNodeTap -> { onNodeTap() }
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

    fun onNodeTap() {
        viewModelScope.launch {
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            navController.popBackStack()
            setEffect { Effect.NavigateBack }
        }
    }

    fun onAddEdgeClick() {
        viewModelScope.launch {
            setState { copy(name, id, graph, StateMode.ADDEDGE) }
            setEffect { Effect.AddEdge }
        }
    }

    fun onAddNodeClick() {
        viewModelScope.launch {
            setState { copy(name, id, graph, StateMode.ADDNODE) }
            setEffect { Effect.AddNode }
        }
    }

    fun onDeleteEdgeClick() {
        viewModelScope.launch {
            setState { copy(name, id, graph, StateMode.DELETEEDGE) }
            setEffect { Effect.DeleteEdge }
        }
    }

    fun onDeleteNodeClick() {
        viewModelScope.launch {
            setState { copy(name, id, graph, StateMode.DELETENODE) }
            setEffect { Effect.DeleteNode }
        }
    }
}