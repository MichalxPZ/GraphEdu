package com.poznan.put.michalxpz.graphedu.data

data class Graph(
    val edges: List<Edge>,
    val num_of_edges: Int,
    val num_of_vertices: Int,
    val vertices: List<Vertice>
)