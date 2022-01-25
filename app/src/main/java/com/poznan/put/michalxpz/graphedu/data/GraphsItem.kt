package com.poznan.put.michalxpz.graphedu.data

import androidx.room.*

@Entity(tableName = "graphsitem")
data class GraphsItem(

    @ColumnInfo(name = "name")
    val name: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}