package com.poznan.put.michalxpz.graphedu.DrawerMenu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poznan.put.michalxpz.graphedu.R
import com.poznan.put.michalxpz.graphedu.data.Graph
import com.poznan.put.michalxpz.graphedu.data.GraphsItem
import com.poznan.put.michalxpz.graphedu.navigation.GraphEduNavigation
import com.poznan.put.michalxpz.graphedu.ui.GraphEduTypography

@Composable
fun DrawerMenu(
    modifier: Modifier = Modifier,
    onDestinationClicked: (route: String) -> Unit,
    graphs: MutableList<GraphsItem>,
    onButtonClick: () -> Unit
) {
    Column() {
        Entries(onDestinationClicked, graphs)
        Spacer(modifier = Modifier.height(24.dp))
        AddGraph(onClick = { onButtonClick() })
    }
}

@Composable
fun AddGraph(onClick : () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Button(onClick = onClick) {
            Text(text = "Create")
        }
    }
}

@Composable
fun Entries(
    onDestinationClicked: (route: String) -> Unit,
    graphs: MutableList<GraphsItem>
) {
    val graphList by remember { mutableStateOf(graphs) }

    Column(Modifier.height(500.dp)) {
        Image(
            painter = painterResource(R.drawable.ic_app_icon),
            contentDescription = "App icon",
            Modifier.size(200.dp),
            contentScale = ContentScale.Crop
        )
        val mainScreen = GraphEduNavigation.values().get(0)
        Spacer(Modifier.height(24.dp))
        Text(
            text = mainScreen.name,
            style = GraphEduTypography.h4,
            modifier = Modifier
                .clickable {
                    onDestinationClicked(mainScreen.name)
                }
                .padding(top = 48.dp, start = 24.dp)
        )
        Spacer(Modifier.height(24.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(start = 24.dp)
        ) {
            item {
                GraphEntriesColumn(graphList, onDestinationClicked)
            }
        }
    }
}

@Composable
private fun GraphEntriesColumn(
    graphList: MutableList<GraphsItem>,
    onDestinationClicked: (route: String) -> Unit
) {
    graphList.forEach { graph ->
        var deleted by remember { mutableStateOf(false) }
        GraphEntryRow(
            {
                GraphEntry(
                    graph = graph,
                    onDestinationClicked = onDestinationClicked,
                    removeGraph = {
                        graphList.remove(graph)
                        deleted = !deleted
                    }
                )
            },
            deleted = deleted
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun GraphEntryRow(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    deleted: Boolean
) {
    AnimatedVisibility(visible = !deleted) {
        Column {
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            ) {
                Layout(
                    content = content,
                    modifier = Modifier
                )
                { measurables, constraints ->
                    val placeables = measurables.map { measurable ->
                        measurable.measure(constraints)
                    }

                    layout(constraints.maxWidth, constraints.maxHeight) {
                        var x_pos = 0
                        placeables.forEach { placeable ->
                            placeable.placeRelative(x = x_pos, y = 0)
                            x_pos = constraints.maxWidth - 150
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GraphEntry(
    graph: GraphsItem,
    onDestinationClicked: (route: String) -> Unit,
    removeGraph: () -> Unit
) {

    Text(
        text = graph.name,
        style = GraphEduTypography.h4,
        modifier = Modifier.clickable {
            onDestinationClicked("${GraphEduNavigation.values().get(1).name}/${graph.id}")
        }
    )
    Image(
        painter = painterResource(id = R.drawable.ic_baseline_delete_24),
        contentDescription = "delete",
        Modifier.clickable(
            onClick = {
                removeGraph()
            }
        )
    )
}

@Preview(name = "graph entry")
@Composable
private fun PreviewGraphEntry() {
    GraphEntry(graph = GraphsItem(1, "name", Graph(listOf(), 0, 0, listOf())), onDestinationClicked = {}, removeGraph = {})
}
