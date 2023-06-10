package com.poznan.put.michalxpz.graphedu.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.db.GraphsDatabase

class GraphRepositoryImpl(
    override val graphsDatabase: GraphsDatabase,
    override val firestore: FirebaseFirestore
) : GraphRepository {
    override fun getAllGraphItems(uid: String): List<GraphsItem> {
        val graphsFromDatabase = graphsDatabase.graphDao.getAllGraphItems(uid)
        val graphsFromFirestore = firestore.collection("graphs")
            .get().result.documents
            .mapNotNull { it.data }
            .filter { it["uid"] == uid }
            .map { GraphsItem(uid, it["name"] as String, it["graphJson"] as String) }
        val needToSyncGraphs = !equalsIgnoreOrder(
            graphsFromDatabase.map { it.name },
            graphsFromFirestore.map { it.name }
        )
        if (needToSyncGraphs) {
            val result = arrayListOf<GraphsItem>()
            graphsFromDatabase.forEach {
                result.add(it)
            }
            graphsFromFirestore.forEach { firestoreGraph ->
                if (!result.map { it.name }.contains(firestoreGraph.name)) {
                    result.add(firestoreGraph)
                }
            }
            graphsFromDatabase.forEach { dbGraph ->
                if (!graphsFromFirestore.map { it.uid }.contains(dbGraph.name)) {
                    val graph = hashMapOf(
                        "uid" to uid,
                        "id" to dbGraph.id,
                        "name" to dbGraph.name,
                        "graphJson" to dbGraph.graphJson
                    )

                    firestore.collection("graphs").add(
                        graph
                    )
                }
            }
            return result
        } else {
            return graphsFromDatabase
        }
    }

    override fun getAllGraphItemsWithoutAuth(): List<GraphsItem> {
        return graphsDatabase.graphDao.getAllGraphItemsWithoutAuth()
    }

    override suspend fun findByName(name: String): GraphsItem {
        return graphsDatabase.graphDao.getAllGraphItemsWithoutAuth().filter { it.name == name }.get(0)
    }

    override suspend fun findGraphItemById(id: Int): GraphsItem {
        return graphsDatabase.graphDao.getAllGraphItemsWithoutAuth().filter { it.id == id }.get(0)
    }

    override suspend fun insertAllGraphItems(vararg graph: GraphsItem) {
        val uid = Firebase.auth.currentUser?.uid ?: "0"
        graphsDatabase.graphDao.insertAllGraphItems(*graph)
        graph.forEach { graphsItem ->
            val graphMap = hashMapOf(
                "uid" to uid,
                "id" to graphsItem.id,
                "name" to graphsItem.name,
                "graphJson" to graphsItem.graphJson
            )

            firestore.collection("graphs").add(
                graphMap
            )
        }
    }

    override suspend fun updateGraph(graph: String, id: Int) {
        val uid = Firebase.auth.currentUser?.uid ?: "0"
        graphsDatabase.graphDao.updateGraph(graph = graph, id)
        val graphInDb = graphsDatabase.graphDao.findGraphItemById(id)
        val graphMap = hashMapOf(
            "uid" to uid,
            "id" to id,
            "name" to graphInDb.name,
            "graphJson" to graph
        )
        val ids = firestore.collection("graphs").get().result.documents
            .filter { it.data != null }
            .filter { it.data?.get("uid") == uid && it.data?.get("name") == graphInDb.name }
            .map { it.id }[0]
        firestore.collection("graphs").document(ids).delete()
        firestore.collection("graphs").add(
            graphMap
        )
    }

    override suspend fun delete(graph: GraphsItem) {
        val uid = Firebase.auth.currentUser?.uid ?: "0"
        graphsDatabase.graphDao.delete(graph)
        val ids = firestore.collection("graphs").get().result.documents
            .filter { it.data != null }
            .filter { it.data?.get("uid") == uid && it.data?.get("name") == graph.name }
            .map { it.id }[0]
        firestore.collection("graphs").document(ids).delete()
    }


    private fun <T> equalsIgnoreOrder(list1:List<T>, list2:List<T>) = list1.size == list2.size && list1.toSet() == list2.toSet()

}