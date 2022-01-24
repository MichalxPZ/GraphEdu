package com.poznan.put.michalxpz.graphedu.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.poznan.put.michalxpz.graphedu.data.Edge
import com.poznan.put.michalxpz.graphedu.data.Graph
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.data.Vertice

@Database(entities = [GraphsItem::class, Graph::class, Edge::class, Vertice::class], version = 1, exportSchema = false)
abstract class GraphsDatabase : RoomDatabase() {
    abstract val graphDao: GraphDao

    companion object {
        @Volatile
        private var INSTANCE: GraphsDatabase? = null

        fun getDataBase(context: Context): GraphsDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    GraphsDatabase::class.java,
                    "graphs_db"
                ).fallbackToDestructiveMigration()
                    .build().also {
                    INSTANCE = it
                }
            }
        }
    }
}