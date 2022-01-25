package com.poznan.put.michalxpz.graphedu.activity

import com.poznan.put.michalxpz.graphedu.base.UiEffect
import com.poznan.put.michalxpz.graphedu.base.UiEvent
import com.poznan.put.michalxpz.graphedu.base.UiState
import com.poznan.put.michalxpz.graphedu.data.GraphsItem


interface MainActivityContract {
    sealed class Effect: UiEffect {
        data class FetchingError(val error: Throwable) : Effect()
    }

    sealed class Event : UiEvent {
    }

    data class State(
        var isLoading: Boolean,
        val graphsItems: ArrayList<GraphsItem>
    ) : UiState {
        companion object {
            val empty = State(true, ArrayList())
        }
    }
}