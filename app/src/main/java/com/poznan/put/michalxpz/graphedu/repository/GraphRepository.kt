package com.poznan.put.michalxpz.graphedu.repository

import android.content.Context

import com.poznan.put.michalxpz.graphedu.data.GraphsItem

class GraphRepository(private val context: Context) {


    fun saveToFile(dataEntities: MutableList<GraphsItem>) {
    }

    fun readFromFile(): MutableList<GraphsItem> {
        return mutableListOf()
    }
}