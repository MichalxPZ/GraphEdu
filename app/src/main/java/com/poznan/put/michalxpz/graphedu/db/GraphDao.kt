package com.poznan.put.michalxpz.graphedu.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.poznan.put.michalxpz.graphedu.data.GraphsItem

@Dao
interface GraphDao {

    @Query("SELECT * FROM graphsitem")
    fun getAll(): List<GraphsItem>

    @Query("SELECT * FROM graphsitem WHERE id IN (:graphIds)")
    fun loadAllByIds(graphIds: IntArray): List<GraphsItem>

    @Query("SELECT * FROM graphsitem WHERE name LIKE :name")
    fun findByName(name: String,): GraphsItem

    @Query("SELECT * FROM graphsitem WHERE id LIKE :id")
    fun findById(id: Int,): GraphsItem

    @Insert
    suspend fun insertAll(vararg graph: GraphsItem)

    @Delete
    fun delete(graph: GraphsItem)
}