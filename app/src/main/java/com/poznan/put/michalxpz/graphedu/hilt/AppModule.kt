package com.poznan.put.michalxpz.graphedu.hilt

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.poznan.put.michalxpz.graphedu.api.QuotesApi
import com.poznan.put.michalxpz.graphedu.db.GraphsDatabase
import com.poznan.put.michalxpz.graphedu.repository.GraphRepository
import com.poznan.put.michalxpz.graphedu.repository.GraphRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


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

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.api-ninjas.com/v1/")
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): QuotesApi = retrofit.create(QuotesApi::class.java)

}