package com.poznan.put.michalxpz.graphedu.activity

import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.runtime.rememberCoroutineScope
import com.poznan.put.michalxpz.graphedu.base.UiEffect
import com.poznan.put.michalxpz.graphedu.base.UiEvent
import com.poznan.put.michalxpz.graphedu.base.UiState
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


interface MainActivityContract {
    sealed class Effect: UiEffect {
        object OpenDialog : Effect()
        object OpenDrawer: Effect()
        object CloseDialog : Effect()
        object EditText : Effect()

        data class FetchingError(val error: Throwable) : Effect()
    }

    sealed class Event : UiEvent {
        object OnOpenDrawerButtonClicked : Event()
        object OnCreateButtonClicked : Event()
        object OnOkDialogClicked: Event()
        object OnDialogTextEdit : Event()
        object OnCloseDialogClicked : Event()
        class OnDeleteButtonClicked(
            val query: GraphsItem
        ) : Event()
    }

    data class State(
        var isLoading: Boolean,
        var graphsItems: ArrayList<GraphsItem>,
        var message: String,
        var openDialog: Boolean,
        var editText: String,
        var drawerState: DrawerState

    ) : UiState {
        companion object {
            val empty = State(
                isLoading = true,
                graphsItems = ArrayList(),
                message = "Name of your graph",
                openDialog = false,
                editText = "",
                drawerState = DrawerState(DrawerValue.Closed)
            )
        }
    }
}