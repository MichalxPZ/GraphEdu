package com.poznan.put.michalxpz.graphedu.activity

import androidx.lifecycle.viewModelScope
import com.poznan.put.michalxpz.graphedu.activity.MainActivityContract.*
import com.poznan.put.michalxpz.graphedu.base.BaseViewModel
import com.poznan.put.michalxpz.graphedu.data.Graph
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.db.GraphsDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val database: GraphsDatabase) : BaseViewModel<Event, State, Effect>() {

    init {
        viewModelScope.launch {
            val graphs = database.graphDao.getAllGraphItems() as ArrayList<GraphsItem>
            delay(3000)
            setState {
                State(
                    isLoading = false,
                    graphsItems = graphs
                )
            }
        }
    }

    override fun createInitialState(): State {
        return MainActivityContract.State.empty
    }

    override fun handleEvent(event: Event) {
    }


    fun addGraph(name: String) {
        viewModelScope.launch {
            val graphsItem = GraphsItem(name)
            database.graphDao.insertAllGraphItems(graphsItem)
            val graph = Graph(0, 0)
            graph.graphId = graphsItem.id
            database.graphDao.insertAllGraphs(graph)
            currentState.graphsItems.add(graphsItem)
        }
    }
}