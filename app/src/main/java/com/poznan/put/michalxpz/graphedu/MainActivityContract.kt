package com.poznan.put.michalxpz.graphedu

import com.poznan.put.michalxpz.graphedu.base.UiEffect
import com.poznan.put.michalxpz.graphedu.base.UiEvent
import com.poznan.put.michalxpz.graphedu.base.UiState


interface MainActivityContract {
    sealed class Effect: UiEffect {
        data class FetchingError(val error: Throwable) : Effect()
    }

    sealed class Event : UiEvent {
    }

    data class State(
        var isLoading: Boolean
    ) : UiState {
        companion object {
            val empty = State(true)
        }
    }
}