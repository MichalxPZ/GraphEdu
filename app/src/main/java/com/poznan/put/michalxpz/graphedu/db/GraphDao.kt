package com.poznan.put.michalxpz.graphedu.db

import androidx.room.*
import com.poznan.put.michalxpz.graphedu.data.Edge
import com.poznan.put.michalxpz.graphedu.data.Graph
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.data.Vertice

@Dao
interface GraphDao {

    @Transaction
    @Query("SELECT * FROM graphsitem")
    suspend fun getAllGraphItems(): List<GraphsItem>

    @Transaction
    @Query("SELECT * FROM graphsitem WHERE id IN (:graphIds)")
    suspend fun loadAllGraphItemsByIds(graphIds: IntArray): List<GraphsItem>

    @Transaction
    @Query("SELECT * FROM graphsitem WHERE name LIKE :name")
    suspend fun findByName(name: String,): GraphsItem

    @Transaction
    @Query("SELECT * FROM graphsitem WHERE id LIKE :id")
    suspend fun findGraphItemById(id: Int,): GraphsItem

    @Transaction
    @Insert
    suspend fun insertAllGraphItems(vararg graph: GraphsItem)

    @Transaction
    @Delete
    suspend fun delete(graph: GraphsItem)

}