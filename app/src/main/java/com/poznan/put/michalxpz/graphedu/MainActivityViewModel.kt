package com.poznan.put.michalxpz.graphedu

import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.poznan.put.michalxpz.graphedu.MainActivityContract.*
import com.poznan.put.michalxpz.graphedu.base.BaseViewModel
import com.poznan.put.michalxpz.graphedu.base.UiEvent
import com.poznan.put.michalxpz.graphedu.base.UiState
import com.poznan.put.michalxpz.graphedu.data.Graph
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.db.GraphDao
import com.poznan.put.michalxpz.graphedu.db.GraphsDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivityViewModel(dao: GraphDao) : BaseViewModel<Event, State, Effect>() {
    init {
        viewModelScope.launch {
            dao.insertAll(GraphsItem(1, "name", Graph(1, 2, 2)))
        }
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