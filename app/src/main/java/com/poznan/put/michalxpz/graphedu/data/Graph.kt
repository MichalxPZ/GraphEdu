package com.poznan.put.michalxpz.graphedu.data

import androidx.room.*

@Entity()
data class Graph(
    @PrimaryKey
    @ColumnInfo(name = "idGraph")
    val id: Int,
    @ColumnInfo(name = "num_of_edges")
    val num_of_vertices: Int,
    @ColumnInfo(name = "num_of_vertices")
    val num_of_edges: Int,
)