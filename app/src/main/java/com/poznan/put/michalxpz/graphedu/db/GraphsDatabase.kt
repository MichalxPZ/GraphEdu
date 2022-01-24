package com.poznan.put.michalxpz.graphedu.db

import android.content.Context
import androidx.room.*
import com.poznan.put.michalxpz.graphedu.converters.GraphTypeConverter
import com.poznan.put.michalxpz.graphedu.data.GraphsItem

@Database(entities = [GraphsItem::class], version = 1)
@TypeConverters(GraphTypeConverter::class)
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
                ).addTypeConverter(GraphTypeConverter())
                    .fallbackToDestructiveMigration()
                    .build().also {
                    INSTANCE = it
                }
            }
        }
    }
}