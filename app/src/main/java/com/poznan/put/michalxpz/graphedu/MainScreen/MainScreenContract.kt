package com.poznan.put.michalxpz.graphedu.MainScreen

import com.poznan.put.michalxpz.graphedu.base.UiEffect
import com.poznan.put.michalxpz.graphedu.base.UiEvent
import com.poznan.put.michalxpz.graphedu.base.UiState

interface MainScreenContract {
    sealed class Effect : UiEffect {
        object NavigateOpenMenu : Effect()
        data class FetchingError(val error: Throwable) : Effect()
    }

    sealed class Event : UiEvent {
        object OnMenuClick : Event()
    }

    data class State(
        //Todo Add statistifcs and welcome screen
        val title: String,
    ) : UiState {
        companion object {
            val empty = State("")
        }
    }
}