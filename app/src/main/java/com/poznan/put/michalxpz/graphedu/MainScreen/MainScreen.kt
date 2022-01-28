package com.poznan.put.michalxpz.graphedu.MainScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.poznan.put.michalxpz.graphedu.components.animatedCircle.AnimatedCircle
import com.poznan.put.michalxpz.graphedu.drawerMenu.TopBar
import com.poznan.put.michalxpz.graphedu.ui.GraphEduTypography
import kotlin.random.Random

@Composable
fun MainScreen(
    graphNum: Int,
    verticesNum: Int,
    edgesNum: Int,
    navController: NavController,
    openDrawer: () -> Unit
) {

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        TopBar(
            title = "GraphEdu",
            buttonIcon = Icons.Filled.Menu,
            onButtonClicked = { openDrawer() }
        )
        Spacer(modifier = Modifier.height(26.dp))
        Box(Modifier.padding(16.dp)) {
            AnimatedCircle(
                modifier = Modifier
                    .height(300.dp)
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                proportions = listOf(1f, graphNum.toFloat()/100, verticesNum.toFloat()/100, edgesNum.toFloat()/100)
            )
            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = "Your summary:",
                    style = GraphEduTypography.h2,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "So far you've created\n$graphNum graphs\n$verticesNum vertices\n$edgesNum edges",
                    style = GraphEduTypography.body1,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
        Spacer(Modifier.height(36.dp))
        Text(
            text = "Did you know...",
            style = GraphEduTypography.h3,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 48.dp)
        )
        val curiosityList = listOf<String>("In any cluster of people, at least two people have the same number of friends.\n" +
                "Friends can be represented by edges of a graph (these edges are not directed). The people in a cluster can be represented by the vertices of the graph and the number of friends of a person is then denoted by the degree of the vertex. (The degree of a vertex is the number of edges which touch the vertex.)\n Try to think about it on your own :)\n\nby Jayanth Ramesh on Quora",
        "Consider a non-empty finite graph (undirected and without self-adjacencies) with the property that any two distinct nodes have precisely one common neighbor. Then there must be some node which is adjacent to every other node. (Indeed, it then follows that the graph must consist of one central node and a bunch of triangles jutting out from that node, like a windmill)\n\nby Sridhar Ramesh on Quora",
            "Dirac’s theorem:\n" +
                    "\n" +
                    "If, in a party with 42 persons, everyone knows at least 21 other partiers, then you can put them in circle such that each of them knows his/her right neighbour",
            "The word “hundred” comes from the old Norse term, “hundrath”, which actually means 120 and not 100 \n\nvia whizz.com blog 20-cool-facts-maths",
            "Most mathematical symbols weren’t invented until the 16th century. Before that, equations were written in words.\n\nvia whizz.com blog 20-cool-facts-math",
            "Every odd number has an “e” in it. \n\nvia whizz.com blog 20-cool-facts-math",
            "If you shuffle a deck of cards properly, it’s more than likely that the exact order of the cards you get has never been seen before in the whole history of the universe.\n\nvia whizz.com blog 20-cool-facts-math",
            )
        val randomValues =  Random.nextInt(0, curiosityList.size)
        Text(
            text = curiosityList.get(randomValues),
            style = GraphEduTypography.body2,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 48.dp)
                .padding(bottom = 24.dp)
        )
    }
}