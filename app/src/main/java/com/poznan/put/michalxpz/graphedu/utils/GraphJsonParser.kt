package com.poznan.put.michalxpz.graphedu.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poznan.put.michalxpz.graphedu.data.Graph
import com.poznan.put.michalxpz.graphedu.data.GraphsItem

class GraphJsonParser() {
    fun parseGraphToJsonString(graph: Graph): String {
        val gson = Gson()
        return gson.toJson(graph)
    }

    fun parseJsonStringToGraph(jsonString: String): Graph {
        val gson = Gson()
        val graphType = object : TypeToken<Graph>() {}.type
        return gson.fromJson(jsonString, graphType)
    }
}