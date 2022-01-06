package com.poznan.put.michalxpz.graphedu.DrawerMenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poznan.put.michalxpz.graphedu.R
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.navigation.GraphEduNavigation
import com.poznan.put.michalxpz.graphedu.ui.GraphEduTypography

@Composable
fun DrawerMenu(
    modifier: Modifier = Modifier,
    onDestinationClicked: (route: String) -> Unit
) {
    val context = LocalContext.current
    val jsonString = context.resources.openRawResource(R.raw.graphs).bufferedReader().readText()
    val gson = Gson()
    val graphsListType = object : TypeToken<List<GraphsItem>>() {}.type
    val graphs: List<GraphsItem> = gson.fromJson(jsonString, graphsListType)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 48.dp)
    ) {
        Image(
            //TODO change icon for graph img
            painter = painterResource(R.drawable.ic_baseline_baby_changing_station_24),
            contentDescription = "App icon"
        )
        val mainScreen = GraphEduNavigation.values().get(0)
        Spacer(Modifier.height(24.dp))
        Text(
            text = mainScreen.name,
            style = GraphEduTypography.h3,
            modifier = Modifier.clickable{
                onDestinationClicked(mainScreen.name)
            }
        )
        graphs.forEach { graph ->
            Spacer(Modifier.height(24.dp))
            Text(
                text = graph.name,
                style = GraphEduTypography.h3,
                modifier = Modifier.clickable{
                    onDestinationClicked("${GraphEduNavigation.values().get(1).name}/${graph.id}")
                }
            )
        }

    }
}