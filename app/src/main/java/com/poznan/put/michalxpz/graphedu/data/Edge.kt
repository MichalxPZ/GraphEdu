package com.poznan.put.michalxpz.graphedu.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [ForeignKey(entity = Graph::class, parentColumns = ["idGraph"], childColumns = ["graphId"])]
)
data class Edge(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "graphId")
    val graphId: Int,
    @ColumnInfo(name = "end")
    val end: Int,
    @ColumnInfo(name = "start")
    val start: Int
)