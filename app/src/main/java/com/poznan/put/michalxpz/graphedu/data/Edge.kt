package com.poznan.put.michalxpz.graphedu.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [ForeignKey(entity = Graph::class, parentColumns = ["graphId"], childColumns = ["graphRefId"])]
)
data class Edge(
    @ColumnInfo(name = "graphRefId")
    val graphId: Int,
    @ColumnInfo(name = "end")
    val end: Int,
    @ColumnInfo(name = "start")
    val start: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}