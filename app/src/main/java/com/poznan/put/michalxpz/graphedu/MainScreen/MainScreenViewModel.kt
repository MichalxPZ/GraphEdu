package com.poznan.put.michalxpz.graphedu.MainScreen

import android.app.Application
import android.content.ContentProvider
import android.content.res.AssetManager
import android.util.JsonToken
import com.poznan.put.michalxpz.graphedu.MainScreen.MainScreenContract.*
import com.poznan.put.michalxpz.graphedu.base.BaseViewModel
import android.content.Context
import java.io.IOException
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poznan.put.michalxpz.graphedu.data.Graph
import dagger.hilt.android.internal.Contexts.getApplication


class MainScreenViewModel() : BaseViewModel<Event, State, Effect>() {
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