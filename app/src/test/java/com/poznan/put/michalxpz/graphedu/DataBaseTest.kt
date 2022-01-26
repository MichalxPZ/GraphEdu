package com.poznan.put.michalxpz.graphedu

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.poznan.put.michalxpz.graphedu.db.GraphDao
import com.poznan.put.michalxpz.graphedu.db.GraphsDatabase

import org.junit.Before
import org.junit.Test
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


class DatabaseTest {
    var database: GraphsDatabase? = null
    var graphDao: GraphDao? = null

    @Before
    @Throws(
        NoSuchMethodException::class,
        InvocationTargetException::class,
        IllegalAccessException::class
    )
    fun setDatabase() {
        val context: Context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, GraphsDatabase::class.java).build()
        val method: Method = GraphsDatabase::class.java.getDeclaredMethod("graphDAO()")
        method.setAccessible(true)
        graphDao = method.invoke(database, null) as GraphDao
    }

    @Test
    fun runDb() {
        
    }
}
