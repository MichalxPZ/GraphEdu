package com.poznan.put.michalxpz.graphedu

import androidx.lifecycle.viewModelScope
import com.poznan.put.michalxpz.graphedu.MainActivityContract.*
import com.poznan.put.michalxpz.graphedu.base.BaseViewModel
import com.poznan.put.michalxpz.graphedu.base.UiEvent
import com.poznan.put.michalxpz.graphedu.base.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivityViewModel : BaseViewModel<Event, State, Effect>() {
    init {
        viewModelScope.launch {
            delay(3000)
            setState {
                State(
                    isLoading = false
                )
            }
        }
    }

    override fun createInitialState(): State {
        return MainActivityContract.State.empty
    }

    override fun handleEvent(event: Event) {
    }
}