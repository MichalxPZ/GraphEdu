package com.poznan.put.michalxpz.graphedu.data

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "graphsitem")
data class GraphsItem(

    @ColumnInfo(name = "uid")
    val uid: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "graphJson")
    val graphJson: String
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}