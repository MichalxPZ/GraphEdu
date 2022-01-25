package com.poznan.put.michalxpz.graphedu.data

import androidx.room.*

@Entity(tableName = "graphs")
class Graph(
    @ColumnInfo(name = "num_of_edges")
    val num_of_vertices: Int,

    @ColumnInfo(name = "num_of_vertices")
    val num_of_edges: Int
) {
    @PrimaryKey(autoGenerate = true)
    var graphId: Int = 0
}