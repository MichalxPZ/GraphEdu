package com.poznan.put.michalxpz.graphedu.converters

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poznan.put.michalxpz.graphedu.data.Graph

class GraphTypeConverter {
    @TypeConverter
    fun fromString(value: String?): Graph? {
        var graph: Graph? = null
        return try {
            val gson = Gson()
            val graphType = object : TypeToken<Graph>() {}.type
            graph = gson.fromJson<Graph>(value, graphType)
            graph
        } catch (e: Exception) {
            Log.e("DB EXCEPTION", e.stackTrace.toString())
            graph
        }
    }

    @TypeConverter
    fun fromGraph(graph: Graph?): String? {
        var result: String? = null
        return try {
            val gson = Gson()
            result = gson.toJson(graph)
            result
        } catch (e: Exception) {
            Log.e("DB EXCEPTION", e.stackTrace.toString())
            result
        }
    }
}