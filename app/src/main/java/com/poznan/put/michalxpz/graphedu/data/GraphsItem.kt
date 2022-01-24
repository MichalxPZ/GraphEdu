package com.poznan.put.michalxpz.graphedu.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.poznan.put.michalxpz.graphedu.converters.GraphTypeConverter

@Entity(tableName = "graphsitem")
data class GraphsItem(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "graph")
    val graph: Graph

)