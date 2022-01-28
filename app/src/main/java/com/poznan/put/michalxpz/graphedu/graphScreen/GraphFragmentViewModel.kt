package com.poznan.put.michalxpz.graphedu.graphScreen


import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.poznan.put.michalxpz.graphedu.graphScreen.GraphFragmentContract.*
import com.poznan.put.michalxpz.graphedu.base.BaseViewModel
import com.poznan.put.michalxpz.graphedu.data.Edge
import com.poznan.put.michalxpz.graphedu.data.Graph
import com.poznan.put.michalxpz.graphedu.data.Vertice
import com.poznan.put.michalxpz.graphedu.db.GraphsDatabase
import com.poznan.put.michalxpz.graphedu.utils.GraphJsonParser
import kotlinx.coroutines.launch
import kotlin.math.abs


class GraphFragmentViewModel (val database: GraphsDatabase, private val graph: Graph, private val navController: NavController, private val graphId: Int) : BaseViewModel<Event, State, Effect>() {


    init {
        viewModelScope.launch {
            try {
                val graphJsonParser = GraphJsonParser()
                val jsonString =
                    database.graphDao.getAllGraphItems().filter { it.id == graphId }.get(0)
                val graphAdd = graphJsonParser.parseJsonStringToGraph(jsonString.graphJson)
                setState { copy(name, id, graphAdd) }
            } catch (e: IndexOutOfBoundsException) {
                setState { copy(name, id, graph, mode, selectedVert) }
            }
        }
    }

    override fun createInitialState(): State {
        return State.empty
    }

    override fun handleEvent(event: Event) {
        when(event) {
            is Event.OnReturnClick -> { navigateBack() }
            is Event.OnCanvasClick -> {
                onCanvasClick(offset = event.offset, event.graph)
                setState { copy(name, id, uiState.value.graph, mode, selectedVert) }
            }
            is Event.OnAddEdgesClick -> { onAddEdgeClick() }
            is Event.OnAddNodeClick -> { onAddNodeClick() }
            is Event.OnNodeDrag -> { onNodeDrag(event.vertice) }
            is Event.OnDeleteEdgeClick -> { onDeleteEdgeClick() }
            is Event.OnDeleteNodeClick -> { onDeleteNodeClick() }
            is Event.OnNodeTap -> { onNodeTap(event.vertice) }
        }
    }

    private fun onCanvasClick(offset: Offset, graph: Graph) {
        viewModelScope.launch {
            updateDatabase(graph)
            setEffect { Effect.CanvasClick }
            println("MODE: ${uiState.value.mode}")
        }
    }

    private fun onNodeDrag(vertice: Vertice) {
        viewModelScope.launch {
            setState { copy(name, id, graph, StateMode.MOVENODE, vertice.vertex_id) }
            setEffect { Effect.NodeDrag }
            println("MODE: ${uiState.value.mode}")
        }
    }

    private fun onNodeTap(vertice: Vertice) {
        viewModelScope.launch {
            setState { copy(name, id, graph, StateMode.MOVENODE, vertice.vertex_id) }
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
            setState { copy(name, id, graph, StateMode.ADDEDGE, selectedVert) }
            println("MODE: ${uiState.value.mode}")
            setEffect { Effect.AddEdge }
        }
    }

    private fun onAddNodeClick() {
        viewModelScope.launch {
            setState { copy(name, id, graph, StateMode.ADDNODE, selectedVert) }
            setEffect { Effect.AddNode }
        }
    }

    private fun onDeleteEdgeClick() {
        viewModelScope.launch {
            setState { copy(name, id, graph, StateMode.DELETEEDGE, selectedVert) }
            println("MODE: ${uiState.value.mode}")
            setEffect { Effect.DeleteEdge }
        }
    }

    private fun onDeleteNodeClick() {
        viewModelScope.launch {
            setState { copy(name, id, graph, StateMode.DELETENODE, selectedVert) }
            println("MODE: ${uiState.value.mode}")
            setEffect { Effect.DeleteNode }
        }
    }

    private fun onTapGesture(
        it: Offset,
        graph: Graph,
    ) {
        viewModelScope.launch {
            val x = it.x
            val y = it.y
            val mapOfVertices = hashMapOf<Int, Pair<Int, Int>>()
            val vertices = graph.vertices
            vertices.forEach { vert ->
                mapOfVertices.put(vert.vertex_id, Pair(vert.x_pos, vert.y_pos))
            }
            Log.i("SELECTED", "SELECTED: $x, $y")
            when(uiState.value.mode) {

                StateMode.DEFAULT -> {
                    mapOfVertices.forEach { (id, offset) ->
                        if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30) {
                            setState { copy(name, id, graph, StateMode.MOVENODE, id) }
                            Log.i("SELECTED", "SELECTED ID: $id")
                        }
                    }
                }

                StateMode.MOVENODE -> {
                    Log.i("SELECTED", "SELECTED DRAG: $x, $y")
                    mapOfVertices.forEach { (id, offset) ->
                        if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30) {
                            setState { copy(name, id, graph, mode, id) }
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
                    setState { copy(name, id, graph, StateMode.DEFAULT, null) }
                    updateGraphState(vertices, graph.edges)
                }

                StateMode.ADDEDGE -> {
                }

                StateMode.ADDNODE -> {
                    Log.i("SELECTED", "SELECTED DRAG: $x, $y")
                    var maxId = 0
                    vertices.forEach { if (it.vertex_id > maxId) maxId = it.vertex_id+1 }
                    vertices.add(Vertice("RED", maxId, x.toInt(), y.toInt()))
                    mapOfVertices.put(maxId, Pair(x.toInt(), y.toInt()))
                    updateGraphState(vertices, graph.edges)
                }

                StateMode.DELETENODE -> {
                    Log.i("SELECTED", "SELECTED DRAG: $x, $y")
                    mapOfVertices.forEach { (id, offset) ->
                        if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30) {
                            vertices.forEach {
                                if (it.vertex_id == id) {
                                    vertices.remove(it)
                                    mapOfVertices.remove(id)
                                }
                            }
                            updateGraphState(vertices, graph.edges)
                            setState { copy(name, id, graph, StateMode.DEFAULT, id) }
                            Log.i("SELECTED", "SELECTED ID: $id")
                        }
                    }
                }

                StateMode.DELETEEDGE -> {
                }
                }
            updateGraphState(vertices, graph.edges)
        }
    }

    fun updateDatabase(graph: Graph) {
        viewModelScope.launch {
            updateGraphState(
                vertices = graph.vertices,
                edges = graph.edges
            )
        }
    }

    private suspend fun updateGraphState(
        vertices: ArrayList<Vertice>,
        edges: ArrayList<Edge>
    ) {
        val mapOfVertices = HashMap<Int, Pair<Int, Int>>()
        vertices.forEach { vert ->
            mapOfVertices.put(vert.vertex_id, Pair(vert.x_pos, vert.y_pos))
        }

        val newGraph = Graph(vertices.size, edges.size, vertices, edges)

        val graphJsonParser = GraphJsonParser()

        val jsonString = graphJsonParser.parseGraphToJsonString(newGraph)
        Log.i("UPDATE DB", "updated ${graph.vertices}")
        Log.i("UPDATE DB", "updated ${jsonString}")
        Log.i("UPDATE DB", "updated ${graphId}")

        database.graphDao.updateGraph(jsonString, graphId)
        setState { copy(name, id, newGraph, mode, selectedVert) }
    }
}