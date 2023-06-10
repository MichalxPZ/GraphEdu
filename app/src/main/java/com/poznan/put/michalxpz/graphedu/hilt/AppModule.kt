package com.poznan.put.michalxpz.graphedu.hilt

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.poznan.put.michalxpz.graphedu.db.GraphsDatabase
import com.poznan.put.michalxpz.graphedu.repository.GraphRepository
import com.poznan.put.michalxpz.graphedu.repository.GraphRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext applicationContext: Context): GraphsDatabase {
        return GraphsDatabase.getDataBase(applicationContext)
    }

    @Provides
    fun provideGraphRepository(@ApplicationContext applicationContext: Context) : GraphRepository {
        return GraphRepositoryImpl(
            graphsDatabase = provideAppDatabase(applicationContext),
            firestore = FirebaseFirestore.getInstance()
        )
    }
}