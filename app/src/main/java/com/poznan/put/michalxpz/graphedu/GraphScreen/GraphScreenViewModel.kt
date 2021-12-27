package com.poznan.put.michalxpz.graphedu.GraphScreen

import com.poznan.put.michalxpz.graphedu.GraphScreen.GraphScreenContract.*
import com.poznan.put.michalxpz.graphedu.MainScreen.MainScreenContract
import com.poznan.put.michalxpz.graphedu.base.BaseViewModel

class GraphScreenViewModel : BaseViewModel<Event, State, Effect>() {
    override fun createInitialState(): State {
        return State.empty
    }

    override fun handleEvent(event: Event) {
        when(event) {
            Event.OnMenuClick -> setEffect { Effect.NavigateOpenMenu }
            Event.OnBackClick -> setEffect { Effect.NavigateBack }
        }
    }

    init {
        fetchData()
    }

    fun fetchData() {
        //TODO update Graph State
        setState {
            GraphScreenContract.State(
                title = ""
            )
        }
    }
}