package com.poznan.put.michalxpz.graphedu.navigation

enum class GraphEduNavigation {
    MainScreen,
    GraphScreen;

    companion object {
        fun fromRoute(route: String?): GraphEduNavigation =
            when(route?.substringBefore("/")) {
                MainScreen.name -> MainScreen
                GraphScreen.name -> GraphScreen
                null -> MainScreen
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}