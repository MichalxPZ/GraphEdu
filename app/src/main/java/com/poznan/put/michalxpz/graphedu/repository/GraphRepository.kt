package com.poznan.put.michalxpz.graphedu.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.db.GraphsDatabase

interface GraphRepository {
    val graphsDatabase: GraphsDatabase
    val firestore: FirebaseFirestore
    fun getAllGraphItems(uid: String): List<GraphsItem>

    fun getAllGraphItemsWithoutAuth(): List<GraphsItem>

    suspend fun findByName(name: String): GraphsItem

    suspend fun findGraphItemById(id: Int): GraphsItem

    suspend fun insertAllGraphItems(vararg graph: GraphsItem)

    suspend fun updateGraph(graph: String, id: Int)

    suspend fun delete(graph: GraphsItem)
}