package com.poznan.put.michalxpz.graphedu.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(entity = Graph::class, parentColumns = ["idGraph"], childColumns = ["graphId"])]
)
data class Vertice(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "graphId")
    val graphId: Int,
    @ColumnInfo(name = "color")
    val color: String,
    @ColumnInfo(name = "vertex_id")
    val vertex_id: Int,
    @ColumnInfo(name = "x_pos")
    val x_pos: Int,
    @ColumnInfo(name = "y_pos")
    val y_pos: Int
)