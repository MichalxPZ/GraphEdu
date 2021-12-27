package com.poznan.put.michalxpz.graphedu.GraphScreen

import com.poznan.put.michalxpz.graphedu.base.UiEffect
import com.poznan.put.michalxpz.graphedu.base.UiEvent
import com.poznan.put.michalxpz.graphedu.base.UiState

interface GraphScreenContract {
    sealed class Effect : UiEffect {
        object NavigateOpenMenu : Effect()
        object NavigateBack : Effect()
        data class FetchingError(val error: Throwable) : Effect()
    }

    sealed class Event : UiEvent {
        object OnMenuClick : Event()
        object OnBackClick : Event()
    }

    data class State(
        //Todo implement graph state
        val title: String,
    ) : UiState {
        companion object {
            val empty = State("")
        }
    }
}