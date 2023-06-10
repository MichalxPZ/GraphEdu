package com.poznan.put.michalxpz.graphedu.api

import com.poznan.put.michalxpz.graphedu.data.Quotes
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface QuotesApi {

    @Headers("X-Api-Key:" + "TtEbgYOsoLnMOl82m67SDw==KVKqeXy6vdZSL9Mx")
    @GET("quotes")
    suspend fun getQuotes(@Query("category") category: String = "knowledge"): Quotes
}