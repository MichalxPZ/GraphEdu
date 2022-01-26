package com.poznan.put.michalxpz.graphedu.activity

import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
            is Event.OnDeleteButtonClicked -> {  }
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
            setEffect { Effect.OpenDrawer }
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

    fun deleteGraph(graphsItem: GraphsItem) {
        viewModelScope.launch {
            val graphsItems = currentState.graphsItems
            graphsItems.remove(graphsItem)
            database.graphDao.delete(graphsItem)
            setState {
                copy(isLoading, graphsItems, message, false, editText, drawerState)
            }
        }
    }

}