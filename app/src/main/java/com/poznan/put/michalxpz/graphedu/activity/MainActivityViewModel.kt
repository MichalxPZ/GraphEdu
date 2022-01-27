package com.poznan.put.michalxpz.graphedu.activity

import android.util.Log
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.viewModelScope
import com.poznan.put.michalxpz.graphedu.activity.MainActivityContract.*
import com.poznan.put.michalxpz.graphedu.base.BaseViewModel
import com.poznan.put.michalxpz.graphedu.data.Edge
import com.poznan.put.michalxpz.graphedu.data.Graph
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.data.Vertice
import com.poznan.put.michalxpz.graphedu.db.GraphsDatabase
import com.poznan.put.michalxpz.graphedu.utils.GraphJsonParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val database: GraphsDatabase) : BaseViewModel<Event, State, Effect>() {

    init {
        viewModelScope.launch {
            currentState.graphsItems = database.graphDao.getAllGraphItems() as ArrayList<GraphsItem>
            delay(3000)
            currentState.isLoading = false
        }
    }

    override fun createInitialState(): State {
        return State.empty
    }

    override fun handleEvent(event: Event) {
        when(event) {
            is Event.OnCreateButtonClicked -> { openDialog() }
            is Event.OnOpenDrawerButtonClicked -> { openOrCloseDrawer() }
            is Event.OnCloseDialogClicked -> { closeDialog() }
            is Event.OnDialogTextEdit -> { editTextField() }
            is Event.OnOkDialogClicked -> { addGraph(uiState.value.editText) }
            is Event.OnDeleteButtonClicked -> { deleteGraph( event.query) }
        }
    }


    private fun openDialog() {
        viewModelScope.launch {
            setState {
                copy(isLoading, graphsItems, message, true, editText, DrawerState(DrawerValue.Closed))
            }
            setEffect { Effect.OpenDialog }
        }
    }

    private fun closeDialog() {
        viewModelScope.launch {
            setState {
                copy(isLoading, graphsItems, message, false, editText, drawerState)
            }

            setEffect { Effect.CloseDialog }
        }
    }

    private fun openOrCloseDrawer() {
        viewModelScope.launch {
            setEffect { Effect.OpenDrawer }
            delay(300)
            if (currentState.drawerState.isOpen) {
                setState {
                    copy(isLoading,
                        graphsItems,
                        message,
                        openDialog,
                        editText,
                        DrawerState(DrawerValue.Closed))
                }
            } else {
                setState {
                    copy(isLoading,
                        graphsItems,
                        message,
                        openDialog,
                        editText,
                        DrawerState(DrawerValue.Open))
                }
            }
        }
    }

    private fun editTextField() {
        viewModelScope.launch {
            setState { copy(isLoading, graphsItems, editText, openDialog, editText, drawerState) }
            setEffect { Effect.EditText }

        }
    }

    private fun addGraph(name: String) {
        viewModelScope.launch {
            val graph = Graph(0, 0, arrayListOf<Vertice>(), arrayListOf<Edge>())
            val graphJsonParser = GraphJsonParser()
            val jsonString = graphJsonParser.parseGraphToJsonString(graph)
            val graphsItem = GraphsItem(name, jsonString)
            database.graphDao.insertAllGraphItems(graphsItem)
            val graphsItems = currentState.graphsItems
            graphsItems.add(graphsItem)
            setState {
                copy(isLoading, graphsItems, message, false, editText, drawerState)
            }
            setEffect { Effect.CloseDialog }
        }
    }

    private fun deleteGraph(graphsItem: GraphsItem) {
        viewModelScope.launch {
            val graphsItems = currentState.graphsItems
            graphsItems.remove(graphsItem)
            database.graphDao.delete(graphsItem)
            setState {
                copy(isLoading, graphsItems, message, false, editText, drawerState)
            }
        }
    }

    private fun onTapGesture(
        it: Offset,
        selectedVert: Int?,
        mapOfVertices: MutableMap<Int, Pair<Int, Int>>,
    ) {
        var selectedVert1 = selectedVert
        val x = it.x
        val y = it.y
        Log.i("SELECTED", "SELECTED: $x, $y")
        if (selectedVert1 == null) {
            mapOfVertices.forEach { (id, offset) ->
                if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30) {
                    selectedVert1 = id
                    Log.i("SELECTED", "SELECTED ID: $id")
                }
            }
        } else {
            Log.i("SELECTED", "SELECTED DRAG: $x, $y")
            mapOfVertices.forEach { (id, offset) ->
                if (abs(offset.first - x) < 30 && abs(offset.second - y) < 30) {
                    selectedVert1 = id
                    Log.i("SELECTED", "SELECTED ID: $id")
                }
            }
            mapOfVertices.put(
                selectedVert1!!,
                Pair(x.toInt(), y.toInt())
            )
            Log.i(
                "SELECTED",
                "DRAG CHANGED TO: ${x + mapOfVertices.get(selectedVert1)!!.first}, ${
                    y + mapOfVertices.get(selectedVert1)!!.second
                }"
            )
            selectedVert1 = null
        }
    }

}