package com.poznan.put.michalxpz.graphedu.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(entity = Graph::class, parentColumns = ["graphId"], childColumns = ["graphRef"])]
)
data class Vertice(
    @ColumnInfo(name = "graphRef")
    val graphId: Int,
    @ColumnInfo(name = "color")
    val color: String,
    @ColumnInfo(name = "vertex_id")
    val vertex_id: Int,
    @ColumnInfo(name = "x_pos")
    val x_pos: Int,
    @ColumnInfo(name = "y_pos")
    val y_pos: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "verticeId")
    var id: Int = 0
}