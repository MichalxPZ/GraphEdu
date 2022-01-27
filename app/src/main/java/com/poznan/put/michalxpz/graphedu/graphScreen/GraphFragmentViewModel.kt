package com.poznan.put.michalxpz.graphedu.graphScreen


import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.poznan.put.michalxpz.graphedu.graphScreen.GraphFragmentContract.*
import com.poznan.put.michalxpz.graphedu.base.BaseViewModel
import com.poznan.put.michalxpz.graphedu.data.Edge
import com.poznan.put.michalxpz.graphedu.data.Graph
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.data.Vertice
import com.poznan.put.michalxpz.graphedu.db.GraphsDatabase
import com.poznan.put.michalxpz.graphedu.utils.GraphJsonParser
import kotlinx.coroutines.launch
import kotlin.math.abs


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
            is Event.OnCanvasClick -> {
                onCanvasClick(offset = event.offset)
                setState { copy(name, id, uiState.value.graph, mode, selectedVert) }
            }
            is Event.OnAddEdgesClick -> { onAddEdgeClick() }
            is Event.OnAddNodeClick -> { onAddNodeClick() }
            is Event.OnNodeDrag -> { onNodeDrag() }
            is Event.OnDeleteEdgeClick -> { onDeleteEdgeClick() }
            is Event.OnDeleteNodeClick -> { onDeleteNodeClick() }
            is Event.OnNodeTap -> { onNodeTap() }
        }
    }

    private fun onCanvasClick(offset: Offset) {
        viewModelScope.launch {
            onTapGesture(offset, uiState.value.graph.vertices)
            setEffect { Effect.CanvasClick }
            println("MODE: ${uiState.value.mode}")
        }
    }

    private fun onNodeDrag() {
        viewModelScope.launch {
            setEffect { Effect.NodeDrag }
            println("MODE: ${uiState.value.mode}")
        }
    }

    private fun onNodeTap() {
        viewModelScope.launch {
            println("MODE: ${uiState.value.mode}")
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            navController.popBackStack()
            setEffect { Effect.NavigateBack }
        }
    }

    private fun onAddEdgeClick() {
        viewModelScope.launch {
            setState { copy(name, id, graph, StateMode.ADDEDGE) }
            println("MODE: ${uiState.value.mode}")
            setEffect { Effect.AddEdge }
        }
    }

    private fun onAddNodeClick() {
        viewModelScope.launch {
            setState { copy(name, id, graph, StateMode.ADDNODE) }
            setEffect { Effect.AddNode }
        }
    }

    private fun onDeleteEdgeClick() {
        viewModelScope.launch {
            setState { copy(name, id, graph, StateMode.DELETEEDGE) }
            println("MODE: ${uiState.value.mode}")
            setEffect { Effect.DeleteEdge }
        }
    }

    private fun onDeleteNodeClick() {
        viewModelScope.launch {
            setState { copy(name, id, graph, StateMode.DELETENODE) }
            println("MODE: ${uiState.value.mode}")
            setEffect { Effect.DeleteNode }
        }
    }

    private suspend fun onTapGesture(
        it: Offset,
        vertices: ArrayList<Vertice>,
    ) {
        val x = it.x
        val y = it.y
        val mapOfVertices = hashMapOf<Int, Pair<Int, Int>>()
        vertices.forEach { vert ->
            mapOfVertices.put(vert.vertex_id, Pair(vert.x_pos, vert.y_pos))
        }
        Log.i("SELECTED", "SELECTED: $x, $y")
        if (uiState.value.selectedVert == null) {
            mapOfVertices.forEach { (id, offset) ->
                if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30) {
                    uiState.value.selectedVert = id
                    Log.i("SELECTED", "SELECTED ID: $id")
                }
            }
        } else {
            Log.i("SELECTED", "SELECTED DRAG: $x, $y")
            mapOfVertices.forEach { (id, offset) ->
                if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30) {
                    uiState.value.selectedVert = id
                    Log.i("SELECTED", "SELECTED ID: $id")
                }
            }
            mapOfVertices.put(
                uiState.value.selectedVert!!,
                Pair(x.toInt(), y.toInt())
            )
            Log.i(
                "SELECTED",
                "DRAG CHANGED TO: ${x + mapOfVertices.get(uiState.value.selectedVert)!!.first}, ${
                    y + mapOfVertices.get(uiState.value.selectedVert)!!.second
                }"
            )
            uiState.value.selectedVert = null
        }
        vertices.forEach{ vertice ->
            vertice.x_pos = mapOfVertices.get(vertice.vertex_id)!!.first
            vertice.y_pos = mapOfVertices.get(vertice.vertex_id)!!.second
        }
        uiState.value.graph.vertices = vertices
        val graphJsonParser = GraphJsonParser()
        val jsonString = graphJsonParser.parseGraphToJsonString(uiState.value.graph)
        database.graphDao.updateGraph(jsonString, uiState.value.id)
    }
}