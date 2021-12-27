package com.poznan.put.michalxpz.graphedu.MainScreen

import com.poznan.put.michalxpz.graphedu.MainScreen.MainScreenContract.*
import com.poznan.put.michalxpz.graphedu.base.BaseViewModel

class MainScreenViewModel : BaseViewModel<Event, State, Effect>() {
    override fun createInitialState(): State {
        return State.empty
    }

    override fun handleEvent(event: Event) {
        when (event) {
            Event.OnMenuClick -> setEffect { Effect.NavigateOpenMenu }
        }
    }

    init {
        fetchData()
    }

    fun fetchData() {
        //TODO update statistics for welcome screen
        setState {
            State(
                title = ""
            )
        }
    }

}