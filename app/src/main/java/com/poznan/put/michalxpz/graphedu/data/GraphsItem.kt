package com.poznan.put.michalxpz.graphedu.data

import androidx.room.*
import com.poznan.put.michalxpz.graphedu.converters.GraphTypeConverter

@Entity(tableName = "graphsitem")
data class GraphsItem(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @Embedded
    val graph: Graph
)