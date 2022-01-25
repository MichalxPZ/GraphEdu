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
    @Query("SELECT * FROM graphs")
    suspend fun getAllGraphs(): List<Graph>

    @Transaction
    @Query("SELECT * FROM graphsitem WHERE id IN (:graphIds)")
    suspend fun loadAllGraphItemsByIds(graphIds: IntArray): List<GraphsItem>

    @Transaction
    @Query("SELECT * FROM graphs WHERE graphId IN (:graphIds)")
    suspend fun loadAllGraphsByIds(graphIds: IntArray): List<Graph>

    @Transaction
    @Query("SELECT * FROM graphsitem WHERE name LIKE :name")
    suspend fun findByName(name: String,): GraphsItem

    @Transaction
    @Query("SELECT * FROM graphsitem WHERE id LIKE :id")
    suspend fun findGraphItemById(id: Int,): GraphsItem
    @Transaction
    @Query("SELECT * FROM graphs WHERE graphId LIKE :id")
    suspend fun findGraphById(id: Int,): Graph

    @Transaction
    @Insert
    suspend fun insertAllGraphItems(vararg graph: GraphsItem)

    @Transaction
    @Insert
    suspend fun insertAllGraphs(vararg graph: Graph)

    @Transaction
    @Insert
    suspend fun insertEdge(vararg edge: Edge)

    @Transaction
    @Insert
    suspend fun insertVertice(vararg vertice: Vertice)

    @Transaction
    @Delete
    suspend fun delete(graph: GraphsItem)

    @Transaction
    @Delete
    suspend fun deleteEdge(edge: Edge)

    @Transaction
    @Delete
    suspend fun deleteVertice(vertice: Vertice)
}